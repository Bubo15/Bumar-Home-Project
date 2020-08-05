package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Business;
import project.bumar.data.entities.Category;
import project.bumar.data.entities.SubCategory;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.data.repositories.SubcategoryRepository;
import project.bumar.event.events.order.DeleteOrderProductsEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.category.CategoryServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.service.models.subcategory.SubcategoryServiceModel;
import project.bumar.service.services.CategoryService;
import project.bumar.service.services.SubcategoryService;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryEditBindingModel;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final Publisher publisher;

    @Autowired
    public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepository, CategoryService categoryService, ModelMapper modelMapper, Publisher publisher) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.publisher = publisher;
    }

    @Override
    public Optional<SubcategoryServiceModel> create(SubcategoryCreateBindingModel subcategoryCreateBindingModel) {

        CategoryServiceModel categoryServiceModel = this.categoryService.getCategoryByName(subcategoryCreateBindingModel.getCategoryName());

        if (categoryServiceModel.getSubCategories().size() > 0) {
            if (this.subcategoryRepository
                    .getSubCategoryByNameAndCategoryName(subcategoryCreateBindingModel.getName(), categoryServiceModel.getName()).isPresent()){

                throw new AlreadyExistException(ErrorConstants.SUBCATEGORY_ALREADY_EXIST);
            }
        }

        Category category = this.modelMapper.map(categoryServiceModel, Category.class);

        SubCategory subCategory = this.modelMapper.map(subcategoryCreateBindingModel, SubCategory.class);

        subCategory.setUrl(categoryServiceModel.getUrl() + "/" + splitName(subcategoryCreateBindingModel.getName().toLowerCase()));
        subCategory.setCategory(category);
        subCategory.setProducts(new HashSet<>());

        category.getSubCategories().add(subCategory);

        this.categoryService.saveCategory(category);
        this.saveSubcategory(subCategory);

        return Optional.of(this.modelMapper.map(subCategory, SubcategoryServiceModel.class));
    }

    @Override
    public Optional<SubcategoryServiceModel> edit(String name, SubcategoryEditBindingModel subcategoryEditBindingModel, String currentCategory) {

        if (!subcategoryEditBindingModel.getCategoryName().isEmpty()) {

            CategoryServiceModel categoryServiceModel = this.categoryService.getCategoryByName(subcategoryEditBindingModel.getCategoryName());

            if (categoryServiceModel
                    .getSubCategories()
                    .stream()
                    .anyMatch(subCategory -> subCategory.getName().equals(
                            subcategoryEditBindingModel.getName().isEmpty() ? name : subcategoryEditBindingModel.getName()))) {

                throw new AlreadyExistException(ErrorConstants.SUBCATEGORY_ALREADY_EXIST);
            }
        }

        SubCategory subCategory = this.subcategoryRepository.getSubCategoryByNameAndCategoryName(name, currentCategory)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.GIVEN_CATEGORY_OR_SUBCATEGORY_NAME_FROM_URL_NOT_FOUND));

        SubCategory subCategoryResult = setData(subcategoryEditBindingModel, subCategory);

        return Optional.of(this.modelMapper.map(subCategoryResult, SubcategoryServiceModel.class));
    }

    @Override
    @Transactional
    public Optional<SubcategoryServiceModel> delete(String name) {
        SubCategory subCategory = this.subcategoryRepository.getSubCategoryByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.SUBCATEGORY_NOT_FOUND));

        Category category = subCategory.getCategory();
        category.getSubCategories().remove(subCategory);

        this.deleteProductsFromBusiness(subCategory);

        this.categoryService.saveCategory(category);
        this.subcategoryRepository.deleteByName(name);

        this.publisher.publishEvent(new DeleteOrderProductsEvent(this,
                subCategory.getProducts().stream()
                        .map(subcategoryProduct -> this.modelMapper.map(subcategoryProduct, BaseProduct.class))
                        .collect(Collectors.toUnmodifiableSet())));

        return Optional.of(this.modelMapper.map(subCategory, SubcategoryServiceModel.class));
    }

    private void deleteProductsFromBusiness(SubCategory subCategory) {
        subCategory.getProducts().forEach(subcategoryProduct -> {

            Business business = subcategoryProduct.getBusiness();
            business.getProducts().remove(subcategoryProduct);
        });
    }

    @Override
    public void saveSubcategory(SubCategory subCategory) {
        this.subcategoryRepository.saveAndFlush(subCategory);
    }

    @Override
    public Optional<List<ProductServiceModel>> getAllProductBySubcategory(String subcategoryName, String categoryName) {
        SubCategory subCategory = this.subcategoryRepository.getSubCategoryByNameAndCategoryName(subcategoryName, categoryName)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.SUBCATEGORY_NOT_FOUND));

        return Optional.of(
                subCategory
                        .getProducts()
                        .stream()
                        .map(subcategoryProduct -> this.modelMapper.map(subcategoryProduct, ProductServiceModel.class))
                        .collect(Collectors.toList()));
    }

    @Override
    public SubcategoryServiceModel getSubcategoryByNameAndCategory(String subcategoryName, String categoryName) {
        return this.modelMapper
                .map(this.subcategoryRepository.getSubCategoryByNameAndCategoryName(subcategoryName, categoryName)
                                .orElseThrow(() -> new NotFoundException(ErrorConstants.GIVEN_CATEGORY_OR_SUBCATEGORY_NAME_FROM_URL_NOT_FOUND)),
                        SubcategoryServiceModel.class);
    }


    private SubCategory setData(SubcategoryEditBindingModel subcategoryEditBindingModel, SubCategory subCategory) {
        if (!subcategoryEditBindingModel.getName().isEmpty()) {
            if (!subcategoryEditBindingModel.getName().equals(subCategory.getName())) {

                subCategory.setName(subcategoryEditBindingModel.getName());
                subCategory.setUrl(subCategory.getCategory().getUrl() + "/" + splitName(subcategoryEditBindingModel.getName().toLowerCase()));
            }
        }

        if (!subcategoryEditBindingModel.getCategoryName().isEmpty()) {
            if (!subcategoryEditBindingModel.getCategoryName().equals(subCategory.getCategory().getName())) {

                Category newCategory =
                        this.modelMapper.map(this.categoryService.getCategoryByName(subcategoryEditBindingModel.getCategoryName()), Category.class);

                Category oldCategory = subCategory.getCategory();
                oldCategory.getSubCategories().remove(subCategory);

                newCategory.getSubCategories().add(subCategory);

                subCategory.setCategory(newCategory);

                if (!subcategoryEditBindingModel.getName().isEmpty()) {

                    subCategory.setUrl(newCategory.getUrl() + "/" + splitName(subcategoryEditBindingModel.getName().toLowerCase()));
                } else {

                    subCategory.setUrl(newCategory.getUrl() + "/" + splitName(subCategory.getName().toLowerCase()));
                }

                this.categoryService.saveCategory(newCategory);
                this.categoryService.saveCategory(oldCategory);

                this.subcategoryRepository.saveAndFlush(subCategory);

            } else if (!subcategoryEditBindingModel.getName().isEmpty()) {
                this.subcategoryRepository.saveAndFlush(subCategory);
            }
        } else if (!subcategoryEditBindingModel.getName().isEmpty()) {
            this.subcategoryRepository.saveAndFlush(subCategory);
        }

        return subCategory;
    }

    private String splitName(String name) {
        return String.join("-", Arrays.asList(name.split(" ")));
    }
}