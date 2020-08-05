package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Business;
import project.bumar.data.entities.Category;
import project.bumar.data.entities.SubcategoryProduct;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.data.repositories.CategoryRepository;
import project.bumar.event.events.order.DeleteOrderProductsEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.category.CategoryCreateServiceModel;
import project.bumar.service.models.category.CategoryServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.service.services.CategoryService;
import project.bumar.web.models.bindingModels.category.CategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.category.CategoryEditBindingModel;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Publisher publisher;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Publisher publisher) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.publisher = publisher;
    }


    @Override
    public List<CategoryServiceModel> getAllCategories() {
        return this.categoryRepository
                .findAll()
                .stream()
                .map(category -> this.modelMapper.map(category, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryCreateServiceModel> create(CategoryCreateBindingModel categoryBinding) {
        if (this.categoryRepository.getCategoryByName(categoryBinding.getName()).isPresent()) {
            throw new AlreadyExistException(ErrorConstants.CATEGORY_ALREADY_EXIST);
        }

        Category category = this.modelMapper.map(categoryBinding, Category.class);

        category.setUrl("/" + this.splitName(categoryBinding.getName().toLowerCase()));
        category.setSubCategories(new HashSet<>());
        category.setProducts(new HashSet<>());

        this.categoryRepository.saveAndFlush(category);
        return Optional.of(this.modelMapper.map(category, CategoryCreateServiceModel.class));
    }

    @Override
    @Transactional
    public Optional<CategoryServiceModel> delete(String name) {

        Category category = this.getNativeCategoryByName(name);

        this.deleteProductsFromBusiness(category);

        this.categoryRepository.deleteByName(name);

        this.publisher.publishEvent(new DeleteOrderProductsEvent(this,
                category
                        .getProducts()
                        .stream()
                        .map(categoryProduct -> this.modelMapper.map(categoryProduct, BaseProduct.class))
                        .collect(Collectors.toUnmodifiableSet())));

        return Optional.of(this.modelMapper.map(category, CategoryServiceModel.class));
    }

    private void deleteProductsFromBusiness(Category category) {
        category.getProducts().forEach(categoryProduct -> {
            Business business = categoryProduct.getBusiness();
            business.getProducts().remove(categoryProduct);
        });
    }

    @Override
    public Optional<CategoryServiceModel> edit(String name, CategoryEditBindingModel categoryEditBindingModel) {

        if (this.categoryRepository.getCategoryByName(categoryEditBindingModel.getName()).isPresent()) {
            throw new AlreadyExistException(ErrorConstants.CATEGORY_ALREADY_EXIST);
        }

        Category category = this.categoryRepository.getCategoryByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.CATEGORY_NAME_TO_EDIT_DOES_NOT_EXIST));

        category.setName(categoryEditBindingModel.getName());
        category.setUrl("/" + this.splitName(categoryEditBindingModel.getName().toLowerCase()));

        this.categoryRepository.saveAndFlush(category);
        return Optional.of(this.modelMapper.map(category, CategoryServiceModel.class));
    }

    @Override
    public CategoryServiceModel getCategoryByName(String categoryName) {
        return this.modelMapper.map(this.getNativeCategoryByName(categoryName), CategoryServiceModel.class);
    }

    @Override
    public void saveCategory(Category category) {
        this.categoryRepository.saveAndFlush(category);
    }

    @Override
    public Optional<List<ProductServiceModel>> getAllProductByCategoryOrSubcategory(String name) {

        List<ProductServiceModel> categoryProduct =
                this
                        .getNativeCategoryByName(name)
                        .getProducts()
                        .stream()
                        .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                        .collect(Collectors.toList());

        if (categoryProduct.isEmpty()) {
            List<ProductServiceModel> categorySubcategoryProducts = new ArrayList<>();

            this
                    .getNativeCategoryByName(name)
                    .getSubCategories()
                    .forEach(subCategory -> categorySubcategoryProducts.add(getRandomProduct(subCategory.getProducts())));

            return Optional.of(categorySubcategoryProducts);
        }

        return Optional.of(categoryProduct);
    }

    @Override
    public boolean isCategoryHasProduct(String name) {
        if (this.getNativeCategoryByName(name).getProducts().size() > 0) {
            return true;
        }

        AtomicBoolean hasProduct = new AtomicBoolean(false);

        this
                .getNativeCategoryByName(name)
                .getSubCategories()
                .forEach(subCategory -> {
                    if (subCategory.getProducts().size() > 0) {
                        hasProduct.set(true);
                    }
                });

        return hasProduct.get();
    }

    private ProductServiceModel getRandomProduct(Set<SubcategoryProduct> products) {
        Random random = new Random();
        int randomProductsCount = random.nextInt(products.size());

        if (randomProductsCount != 0) {
            randomProductsCount -= 1;
        }

        List<ProductServiceModel> productServiceModels =
                products
                        .stream()
                        .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                        .collect(Collectors.toList());

        Collections.shuffle(productServiceModels);

        return productServiceModels.get(randomProductsCount);
    }

    private String splitName(String name) {
        return String.join("-", Arrays.asList(name.split(" ")));
    }

    private Category getNativeCategoryByName(String name) {
        return this.categoryRepository.getCategoryByName(name).orElseThrow(() -> new NotFoundException(ErrorConstants.CATEGORY_NOT_FOUND));
    }
}
