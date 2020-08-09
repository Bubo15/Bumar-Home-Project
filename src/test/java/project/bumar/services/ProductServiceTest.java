package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.*;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.data.repositories.*;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.EmptyKeyOrValueException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.product.ProductDetailsServiceModel;
import project.bumar.services.models.product.ProductServiceModel;
import project.bumar.services.services.*;
import project.bumar.services.services.impl.*;
import project.bumar.validate.Validator;
import project.bumar.validate.impl.ValidatorImpl;
import project.bumar.web.models.bindingModels.product.ProductCreateBindingModel;
import project.bumar.web.models.bindingModels.product.ProductEditBindingModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private NewsRepository newsRepository;

    private ProductService productService;

    private BusinessService businessService;

    private CategoryService categoryService;

    private SubcategoryService subcategoryService;

    private Validator validator;

    private NewsService newsService;

    @Mock
    private FileUploaderService fileUploaderService;

    @Mock
    private Publisher publisher;


    @Before
    public void beforeEach(){
        ModelMapper modelMapper = new ModelMapper();

        newsService = new NewsServiceImpl(newsRepository ,fileUploaderService,publisher, modelMapper);
        businessService = new BusinessServiceImpl(modelMapper, businessRepository, fileUploaderService, publisher);
        validator = new ValidatorImpl(newsService ,businessService);
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper, publisher);
        subcategoryService = new SubcategoryServiceImpl(subcategoryRepository, categoryService, modelMapper, publisher);
        productService = new ProductServiceImpl(productRepository, categoryService, businessService, fileUploaderService, subcategoryService, publisher, modelMapper, validator);
    }

    @Test
    public void createCategoryProduct_shouldCreateCategoryProduct_whenGiveValidData(){
        Category category  = new Category();
        category.setName("Category");
        category.setProducts(new HashSet<>());

        categoryRepository.save(category);

        Business business = new Business();
        business.setName("Business");
        business.setProducts(new HashSet<>());

        businessRepository.save(business);

        ProductCreateBindingModel productCreateBindingModel = createProductCreateBindingModel();
        productCreateBindingModel.setDescription(List.of(new HashMap<>()));

        Optional<ProductServiceModel> productServiceModel = productService.createCategoryProduct(productCreateBindingModel, null, category.getName());

        assertEquals(productCreateBindingModel.getName(), productServiceModel.get().getName());
    }

    @Test
    public void createCategoryProduct_shouldThrowException_whenInCategoryAlreadyExistProduct(){
        CategoryProduct product = new CategoryProduct();

        productRepository.saveAndFlush(product);

        Category category  = new Category();
        category.setName("Category");
        category.setProducts(Set.of(product));

        categoryRepository.save(category);

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            productService.createCategoryProduct(new ProductCreateBindingModel(), null, category.getName());
        });

        String expectedMessage = ErrorConstants.PRODUCT_ALREADY_EXIST_IN_THIS_CATEGORY;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void createCategoryProduct_shouldThrowException_whenGiveInvalidDescription(){
        Category category  = new Category();
        category.setName("Category");
        category.setProducts(new HashSet<>());

        categoryRepository.save(category);

        ProductCreateBindingModel productCreateBindingModel = createProductCreateBindingModel();
        productCreateBindingModel.setDescription(List.of(new HashMap<>(){{put("key", "");}}));

        BaseCustomException exception = assertThrows(EmptyKeyOrValueException.class, () -> {
            productService.createCategoryProduct(productCreateBindingModel, null, category.getName());
        });

        String expectedMessage = ErrorConstants.PRODUCT_DESCRIPTION_EMPTY_KEY_OR_VALUE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void createCategoryProduct_shouldThrowException_whenGiveInvalidBusiness(){
        Category category  = new Category();
        category.setName("Category");
        category.setProducts(new HashSet<>());

        categoryRepository.save(category);

        ProductCreateBindingModel productCreateBindingModel = createProductCreateBindingModel();
        productCreateBindingModel.setDescription(List.of(new HashMap<>()));

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.createCategoryProduct(productCreateBindingModel, null, category.getName());
        });

        String expectedMessage = ErrorConstants.GIVEN_BUSINESS_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void createSubcategoryProduct_shouldCreateProduct_whenGiveValidData(){
        Category category  = new Category();
        category.setName("Category");
        category.setProducts(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory  = new SubCategory();
        subCategory.setName("SubCategory");
        subCategory.setProducts(new HashSet<>());
        subCategory.setCategory(category);

        subcategoryRepository.saveAndFlush(subCategory);

        Business business = new Business();
        business.setName("Business");
        business.setProducts(new HashSet<>());

        businessRepository.save(business);

        ProductCreateBindingModel productCreateBindingModel = createProductCreateBindingModel();
        productCreateBindingModel.setDescription(List.of(new HashMap<>()));

        Optional<ProductServiceModel> productServiceModel = productService.createSubcategoryProduct(productCreateBindingModel, null, category.getName(),subCategory.getName());

        assertEquals(productCreateBindingModel.getName(), productServiceModel.get().getName());
    }

    @Test
    public void createSubcategoryProduct_shouldThrowException_whenGiveNotExistingCategoryOrSubcategory(){
        ProductCreateBindingModel productCreateBindingModel = createProductCreateBindingModel();
        productCreateBindingModel.setDescription(List.of(new HashMap<>()));

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.createSubcategoryProduct(productCreateBindingModel, null, "NotExist", "NotExist");
        });

        String expectedMessage = ErrorConstants.GIVEN_CATEGORY_OR_SUBCATEGORY_NAME_FROM_URL_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void createSubcategoryProduct_shouldThrowException_whenGiveAlreadyExistingProductInSubcategory(){
        SubcategoryProduct subcategoryProduct = new SubcategoryProduct();

        productRepository.saveAndFlush(subcategoryProduct);

        Category category  = new Category();
        category.setName("Category");
        category.setProducts(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory  = new SubCategory();
        subCategory.setName("SubCategory");
        subCategory.setProducts(Set.of(subcategoryProduct));
        subCategory.setCategory(category);

        subcategoryRepository.saveAndFlush(subCategory);

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            productService.createSubcategoryProduct(new ProductCreateBindingModel(), null, category.getName(), subCategory.getName());
        });

        String expectedMessage = ErrorConstants.PRODUCT_ALREADY_EXIST_IN_THIS_SUBCATEGORY;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteCategoryProduct_shouldDeleteCategoryProduct_whenGiveValidId(){
        Category category  = new Category();
        category.setName("Category");
        category.setProducts(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        Business business = new Business();
        business.setName("Business");
        business.setProducts(new HashSet<>());

        businessRepository.save(business);

        CategoryProduct categoryProduct = new CategoryProduct();
        categoryProduct.setName("categoryProduct");
        categoryProduct.setCategory(category);
        categoryProduct.setBusiness(business);

        productRepository.saveAndFlush(categoryProduct);

        Optional<ProductServiceModel> productServiceModel = productService.deleteCategoryProduct(categoryProduct.getId());

        assertEquals(productServiceModel.get().getName(), "categoryProduct");
        assertEquals(0, productRepository.count());
    }

    @Test
    public void deleteCategoryProduct_shouldThrowException_whenGiveInvalidId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.deleteCategoryProduct(-1);
        });

        String expectedMessage = ErrorConstants.PRODUCT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteSubcategoryProduct_shouldDeleteSubcategoryProduct_whenGiveValidId(){
        SubCategory subCategory = new SubCategory();
        subCategory.setName("SubCategory");
        subCategory.setProducts(new HashSet<>());

        subcategoryRepository.saveAndFlush(subCategory);

        Business business = new Business();
        business.setName("Business");
        business.setProducts(new HashSet<>());

        businessRepository.save(business);

        SubcategoryProduct subcategoryProduct = new SubcategoryProduct();
        subcategoryProduct.setName("subcategoryProduct");
        subcategoryProduct.setSubcategory(subCategory);
        subcategoryProduct.setBusiness(business);

        productRepository.saveAndFlush(subcategoryProduct);

        Optional<ProductServiceModel> productServiceModel = productService.deleteCategoryProduct(subcategoryProduct.getId());

        assertEquals(productServiceModel.get().getName(), "subcategoryProduct");
        assertEquals(0, productRepository.count());
    }

    @Test
    public void deleteSubcategoryProduct_shouldThrowException_whenGiveInvalidId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.deleteCategoryProduct(-1);
        });

        String expectedMessage = ErrorConstants.PRODUCT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldEditProduct_whenGiveValidData(){
        Business business = new Business();
        business.setName("Business");
        business.setProducts(new HashSet<>());

        businessRepository.save(business);

        BaseProduct baseProduct = new CategoryProduct();
        baseProduct.setBusiness(business);

        productRepository.saveAndFlush(baseProduct);


        ProductEditBindingModel productEditBindingModel = new ProductEditBindingModel();

        productEditBindingModel.setName("Product");
        productEditBindingModel.setMainDescription("MainDescription");
        productEditBindingModel.setBusinessName("Business");
        productEditBindingModel.setCountOfProduct(10);
        productEditBindingModel.setPrice(100);
        productEditBindingModel.setDescription(List.of(new HashMap<>()));

        Optional<ProductDetailsServiceModel> productDetailsServiceModel = productService.edit(productEditBindingModel, baseProduct.getId(), null);

        assertEquals(baseProduct.getName(), productDetailsServiceModel.get().getName());
    }

    @Test
    public void edit_shouldThrowException_whenGiveNotExistingBusiness(){
        BaseProduct baseProduct = new CategoryProduct();

        productRepository.saveAndFlush(baseProduct);

        ProductEditBindingModel productEditBindingModel = new ProductEditBindingModel();

        productEditBindingModel.setBusinessName("Business");
        productEditBindingModel.setDescription(List.of(new HashMap<>()));

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.edit(productEditBindingModel, baseProduct.getId(), null);
        });

        String expectedMessage = ErrorConstants.GIVEN_BUSINESS_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldThrowException_whenGiveNotExistingProductId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.edit(new ProductEditBindingModel(), -1, null);
        });

        String expectedMessage = ErrorConstants.PRODUCT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getProductById_shouldReturnProduct_whenGiveValidData(){
        BaseProduct baseProduct = new CategoryProduct();
        baseProduct.setName("Product");

        productRepository.saveAndFlush(baseProduct);

        BaseProduct baseProductReturned = productService.getProductById(baseProduct.getId());

        assertEquals(baseProduct.getName(), baseProductReturned.getName());
    }

    @Test
    public void getProductById_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.getProductById(-1);
        });

        String expectedMessage = ErrorConstants.PRODUCT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getProductDetailsById_shouldReturnProductDetails_whenGiveValidData(){
        BaseProduct baseProduct = new CategoryProduct();
        baseProduct.setName("Product");

        productRepository.saveAndFlush(baseProduct);

        Optional<ProductDetailsServiceModel> baseProductReturned = productService.getProductDetailsById(baseProduct.getId());

        assertEquals(baseProduct.getName(), baseProductReturned.get().getName());

    }

    @Test
    public void getProductDetailsById_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.getProductDetailsById(-1);
        });

        String expectedMessage = ErrorConstants.PRODUCT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteDescriptionOfProductByKey_shouldDeleteDescByKey_whenGiveValidData(){
        BaseProduct baseProduct = new CategoryProduct();
        baseProduct.setName("Product");
        baseProduct.setDescription(new HashMap<>(){{put("key", "value");}});

        productRepository.saveAndFlush(baseProduct);

        Optional<ProductDetailsServiceModel> productDetailsServiceModel = productService.deleteDescriptionOfProductByKey("key", baseProduct.getId());

        assertEquals(0, baseProduct.getDescription().entrySet().size());
    }

    @Test
    public void deleteDescriptionOfProductByKey_shouldThrowException_whenGiveNotExistingKey(){
        BaseProduct baseProduct = new CategoryProduct();
        baseProduct.setName("Product");
        baseProduct.setDescription(new HashMap<>());

        productRepository.saveAndFlush(baseProduct);

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.deleteDescriptionOfProductByKey("key", baseProduct.getId());
        });

        String expectedMessage = ErrorConstants.GIVEN_PRODUCT_DESCRIPTION_KEY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteDescriptionOfProductByKey_shouldThrowException_whenGiveInvalidId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            productService.deleteDescriptionOfProductByKey("key", -1);
        });

        String expectedMessage = ErrorConstants.PRODUCT_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllNewProduct_shouldReturnAllNewProduct(){
        BaseProduct baseProduct = new CategoryProduct();
        baseProduct.setName("Product");
        baseProduct.setNew(true);

        productRepository.saveAndFlush(baseProduct);

        List<ProductServiceModel> productServiceModels = productService.getAllNewProduct();

        assertEquals(1, productServiceModels.size());
    }

    private ProductCreateBindingModel createProductCreateBindingModel() {
        ProductCreateBindingModel productCreateBindingModel = new ProductCreateBindingModel();

        productCreateBindingModel.setName("Product");
        productCreateBindingModel.setMainDescription("MainDescription");
        productCreateBindingModel.setBusinessName("Business");
        productCreateBindingModel.setCountOfProduct(10);
        productCreateBindingModel.setPrice(100);

        return productCreateBindingModel;
    }
}
