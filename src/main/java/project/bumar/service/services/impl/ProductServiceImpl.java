package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.constant.ErrorConstants;
import project.bumar.constant.FileUploaderConstants;
import project.bumar.constant.ProductConstant;
import project.bumar.data.entities.*;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.data.repositories.ProductRepository;
import project.bumar.event.events.email.SendProductCreatedEmailEvent;
import project.bumar.event.events.file.DeleteImageEvent;
import project.bumar.event.events.order.DeleteOrderProductsEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.EmptyKeyOrValueException;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.product.ProductDetailsServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.service.services.*;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.product.ProductCreateBindingModel;
import project.bumar.web.models.bindingModels.product.ProductEditBindingModel;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BusinessService businessService;
    private final FileUploaderService fileUploaderService;
    private final SubcategoryService subcategoryService;
    private final Publisher publisher;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, BusinessService businessService, FileUploaderService fileUploaderService, SubcategoryService subcategoryService, Publisher publisher, ModelMapper modelMapper, Validator validator) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.businessService = businessService;
        this.fileUploaderService = fileUploaderService;
        this.subcategoryService = subcategoryService;
        this.publisher = publisher;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public Optional<ProductServiceModel> createCategoryProduct(ProductCreateBindingModel productCreateBindingModel, MultipartFile file, String categoryName) {

        Category category = this.modelMapper.map(this.categoryService.getCategoryByName(categoryName), Category.class);

        if (category.getProducts()
                .stream()
                .map(BaseProduct::getName)
                .collect(Collectors.toList())
                .contains(productCreateBindingModel.getName())) {

            throw new AlreadyExistException(ErrorConstants.PRODUCT_ALREADY_EXIST_IN_THIS_CATEGORY);
        }


        CategoryProduct product =
                this.modelMapper.map(productCreateBindingModel, CategoryProduct.class);

        product.setCategory(category);

        setCreateData(productCreateBindingModel, product, file);

        category.getProducts().add(product);

        this.categoryService.saveCategory(category);

        return Optional.of(this.modelMapper.map(product, ProductServiceModel.class));
    }


    @Override
    public Optional<ProductServiceModel> createSubcategoryProduct(ProductCreateBindingModel productCreateBindingModel, MultipartFile file, String categoryName, String subcategoryName) {

        SubCategory subCategory =
                this.modelMapper.map(this.subcategoryService.getSubcategoryByNameAndCategory(subcategoryName, categoryName), SubCategory.class);

        if (subCategory.getProducts()
                .stream()
                .map(BaseProduct::getName)
                .collect(Collectors.toList())
                .contains(productCreateBindingModel.getName())) {

            throw new AlreadyExistException(ErrorConstants.PRODUCT_ALREADY_EXIST_IN_THIS_SUBCATEGORY);
        }

        SubcategoryProduct product =
                this.modelMapper.map(productCreateBindingModel, SubcategoryProduct.class);

        product.setSubcategory(subCategory);

        setCreateData(productCreateBindingModel, product, file);

        subCategory.getProducts().add(product);

        this.subcategoryService.saveSubcategory(subCategory);

        return Optional.of(this.modelMapper.map(product, ProductServiceModel.class));
    }

    @Override
    @Transactional
    public Optional<ProductServiceModel> deleteCategoryProduct(long id) {

        BaseProduct baseProduct = this.getProductById(id);

        CategoryProduct product = this.modelMapper.map(baseProduct, CategoryProduct.class);

        if (product.getCategory() == null) {
            return this.deleteSubcategoryProduct(id);
        } else {

            ProductServiceModel productServiceModel = this.modelMapper.map(product, ProductServiceModel.class);

            Category category = product.getCategory();
            Business business = product.getBusiness();

            this.publisher.publishEvent(new DeleteImageEvent(this, product.getId()));
            this.publisher.publishEvent(new DeleteOrderProductsEvent(this, Set.of(baseProduct)));

            category.getProducts().remove(product);
            business.getProducts().remove(product);

            this.productRepository.delete(baseProduct);
            this.businessService.saveBusiness(business);
            this.categoryService.saveCategory(category);

            return Optional.of(productServiceModel);
        }
    }

    @Override
    @Transactional
    public Optional<ProductServiceModel> deleteSubcategoryProduct(long id) {

        BaseProduct baseProduct = this.getProductById(id);

        SubcategoryProduct product = this.modelMapper.map(baseProduct, SubcategoryProduct.class);

        ProductServiceModel productServiceModel = this.modelMapper.map(product, ProductServiceModel.class);

        SubCategory subCategory = product.getSubcategory();
        Business business = product.getBusiness();

        subCategory.getProducts().remove(product);
        business.getProducts().remove(product);

        this.publisher.publishEvent(new DeleteImageEvent(this, product.getId()));
        this.publisher.publishEvent(new DeleteOrderProductsEvent(this, Set.of(baseProduct)));

        this.productRepository.delete(baseProduct);
        this.businessService.saveBusiness(business);
        this.subcategoryService.saveSubcategory(subCategory);

        return Optional.of(productServiceModel);
    }

    @Override
    public Optional<ProductDetailsServiceModel> getProductDetailsById(long id) {

        BaseProduct product = this.getProductById(id);

        return Optional.of(this.modelMapper.map(product, ProductDetailsServiceModel.class));
    }

    @Override
    public Optional<ProductDetailsServiceModel> edit(ProductEditBindingModel productEditBindingModel, long id, MultipartFile file) {

        if (this.productRepository.getBaseProductById(id).isEmpty()) {
            throw new NotFoundException(ErrorConstants.PRODUCT_ID_NOT_FOUND);
        }

        BaseProduct newProduct = this.setEditData(productEditBindingModel, id, file);

        this.productRepository.saveAndFlush(newProduct);

        return Optional.of(this.modelMapper.map(newProduct, ProductDetailsServiceModel.class));
    }

    @Override
    public BaseProduct getProductById(long id) {
        return this.productRepository.getBaseProductById(id).orElseThrow(() -> new NotFoundException(ErrorConstants.PRODUCT_ID_NOT_FOUND));
    }

    @Override
    public Optional<ProductDetailsServiceModel> deleteDescriptionOfProductByKey(String key, long productId) {

        BaseProduct product = this.getProductById(productId);

        if (!product.getDescription().containsKey(key)){
            throw new NotFoundException(ErrorConstants.GIVEN_PRODUCT_DESCRIPTION_KEY_NOT_FOUND);
        }

        product.getDescription().remove(key);

        this.productRepository.saveAndFlush(product);

        return Optional.of(this.modelMapper.map(product, ProductDetailsServiceModel.class));
    }

    @Override
    public void saveProduct(BaseProduct product) {
        this.productRepository.saveAndFlush(product);
    }

    @Override
    public List<ProductServiceModel> getAllNewProduct() {
        List<ProductServiceModel> newProducts = this.productRepository
                .findAll()
                .stream()
                .filter(BaseProduct::isNew)
                .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());

        Collections.shuffle(newProducts);

        return newProducts;
    }

    private Map<String, String> setDescription(List<Map<String, String>> newDescriptions, Map<String, String> description) {
        for (Map<String, String> map : newDescriptions) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                    description.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        }

        return description;
    }

    private BaseProduct setEditData(ProductEditBindingModel productEditBindingModel, long id, MultipartFile file) {
        Business business = null;

        if (!productEditBindingModel.getBusinessName().isEmpty()) {
            business = this.modelMapper.map(this.businessService.getBusinessByName(productEditBindingModel.getBusinessName()), Business.class);

            if (business == null) {
                throw new NotFoundException(ErrorConstants.GIVEN_BUSINESS_NOT_FOUND);
            }
        }

        BaseProduct product = this.getProductById(id);

        if (!productEditBindingModel.getName().isEmpty()) {
            product.setName(productEditBindingModel.getName());
        }

        if (!productEditBindingModel.getMainDescription().isEmpty()) {
            product.setMainDescription(productEditBindingModel.getMainDescription());
        }

        if (productEditBindingModel.getPrice() != 0) {
            product.setPrice(productEditBindingModel.getPrice());
        }

        if (productEditBindingModel.getCountOfProduct() != 0) {
            product.setCountOfProduct(productEditBindingModel.getCountOfProduct());
        }

        if (business != null && !business.getName().equals(product.getBusiness().getName())) {
            product.setBusiness(business);
        }

        if (file != null) {
            this.publisher.publishEvent(new DeleteImageEvent(this, product.getId()));

            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.PRODUCT_FOLDER_NAME, product.getId(), file);

            Picture picture = new Picture();
            picture.setUrl(uploadedFileUrl);

            product.setPicture(picture);
        }

        product.setDescription(setDescription(productEditBindingModel.getDescription(), product.getDescription()));

        return product;
    }

    private void setCreateData(ProductCreateBindingModel productCreateBindingModel, BaseProduct product, MultipartFile file) {

        if (validator.isThereProductDescriptionErrors(productCreateBindingModel.getDescription())) {
            throw new EmptyKeyOrValueException(ErrorConstants.PRODUCT_DESCRIPTION_EMPTY_KEY_OR_VALUE);
        }

        Business business = this.modelMapper.map(
                this.businessService.getBusinessByName(productCreateBindingModel.getBusinessName()), Business.class);

        product.setBusiness(business);
        product.setDescription(setDescription(productCreateBindingModel.getDescription(), new HashMap<>()));
        product.setCreated(LocalDateTime.now());
        product.setNew(true);

        this.productRepository.save(product);

        Picture picture = new Picture();

        if (file == null) {
            picture.setUrl(ProductConstant.DEFAULT_PICTURE);
            product.setPicture(picture);
        } else {
            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.PRODUCT_FOLDER_NAME, product.getId(), file);
            picture.setUrl(uploadedFileUrl);

            product.setPicture(picture);
        }

        business.getProducts().add(product);

        this.publisher.publishEvent(new SendProductCreatedEmailEvent(this, product));

        this.businessService.saveBusiness(business);
        this.productRepository.saveAndFlush(product);
    }
}
