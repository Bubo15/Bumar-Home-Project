package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Business;
import project.bumar.data.entities.Category;
import project.bumar.data.entities.CategoryProduct;
import project.bumar.data.repositories.BusinessRepository;
import project.bumar.data.repositories.CategoryRepository;
import project.bumar.data.repositories.ProductRepository;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.category.CategoryCreateServiceModel;
import project.bumar.services.models.category.CategoryServiceModel;
import project.bumar.services.models.product.ProductServiceModel;
import project.bumar.services.services.CategoryService;
import project.bumar.services.services.impl.CategoryServiceImpl;
import project.bumar.web.models.bindingModels.category.CategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.category.CategoryEditBindingModel;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private Publisher publisher;

    private CategoryService categoryService;

    @Before
    public void beforeEach() {

        categoryService = new CategoryServiceImpl(categoryRepository, new ModelMapper(), publisher);
    }

    @Test
    public void getAllCategories_shouldReturnAllCategories() {
        categoryRepository.saveAll(List.of(new Category(), new Category()));

        List<CategoryServiceModel> categoryServiceModels = categoryService.getAllCategories();

        assertEquals(2, categoryServiceModels.size());
    }

    @Test
    public void create_shouldCreateCategory_whenGiveValidData() {
        CategoryCreateBindingModel categoryCreateBindingModel = new CategoryCreateBindingModel();
        categoryCreateBindingModel.setName("Category");

        Optional<CategoryCreateServiceModel> categoryCreateServiceModel = categoryService.create(categoryCreateBindingModel);

        assertEquals(categoryCreateServiceModel.get().getName(), categoryCreateBindingModel.getName());
        assertEquals(1, categoryRepository.count());
    }

    @Test
    public void create_shouldThrowException_whenGiveAlreadyExistingCategory() {
        Category category = new Category();
        category.setName("Category");

        categoryRepository.save(category);

        CategoryCreateBindingModel categoryCreateBindingModel = new CategoryCreateBindingModel();
        categoryCreateBindingModel.setName("Category");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            categoryService.create(categoryCreateBindingModel);
        });

        String expectedMessage = ErrorConstants.CATEGORY_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void delete_deleteCategory_whenGiveValidData(){
        Business business = new Business();
        businessRepository.save(business);

        CategoryProduct categoryProduct = new CategoryProduct();
        categoryProduct.setBusiness(business);

        productRepository.saveAndFlush(categoryProduct);

        business.setProducts(Set.of(categoryProduct));

        Category category = new Category();
        category.setName("Category");
        category.setProducts(Set.of(categoryProduct));

        categoryRepository.save(category);

        Optional<CategoryServiceModel> categoryServiceModel = categoryService.delete("Category");

        assertEquals(categoryServiceModel.get().getName(), category.getName());
        assertEquals(0, business.getProducts().size());
    }

    @Test
    public void delete_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.delete("Category");
        });

        String expectedMessage = ErrorConstants.CATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldEditCategory_whenGiveValidData(){
        Category category = new Category();
        category.setName("Category");

        categoryRepository.save(category);

        CategoryEditBindingModel categoryEditBindingModel = new CategoryEditBindingModel();
        categoryEditBindingModel.setName("Category1");

        Optional<CategoryServiceModel> categoryServiceModel = categoryService.edit(category.getName(), categoryEditBindingModel);

        assertEquals(categoryServiceModel.get().getName(), categoryEditBindingModel.getName());
    }

    @Test
    public void edit_shouldThrowException_whenGiveAlreadyExistingCategory(){
        Category category = new Category();
        category.setName("Category");

        categoryRepository.save(category);

        CategoryEditBindingModel categoryEditBindingModel = new CategoryEditBindingModel();
        categoryEditBindingModel.setName("Category");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            categoryService.edit(category.getName(), categoryEditBindingModel);
        });

        String expectedMessage = ErrorConstants.CATEGORY_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldThrowException_whenGiveInvalidCurrentCategoryName(){
        Category category = new Category();
        category.setName("Category");

        categoryRepository.save(category);

        CategoryEditBindingModel categoryEditBindingModel = new CategoryEditBindingModel();
        categoryEditBindingModel.setName("Category1");

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.edit("NotFoundName", categoryEditBindingModel);
        });

        String expectedMessage = ErrorConstants.CATEGORY_NAME_TO_EDIT_DOES_NOT_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getCategoryByName_shouldReturnCategory_whenGiveValidData(){
        Category category = new Category();
        category.setName("Category");

        categoryRepository.save(category);

        CategoryServiceModel categoryServiceModel = categoryService.getCategoryByName(category.getName());

        assertEquals(categoryServiceModel.getName(), category.getName());
    }

    @Test
    public void getCategoryByName_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.getCategoryByName("Name");
        });

        String expectedMessage = ErrorConstants.CATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllProductByCategoryOrSubcategory_shouldReturnProductsByCatOrSubCat_whenGiveValidData(){
        Category category = new Category();
        category.setName("Category");

        category.setProducts(Set.of(new CategoryProduct(), new CategoryProduct()));

        categoryRepository.save(category);

        Optional<List<ProductServiceModel>> productServiceModels = categoryService.getAllProductByCategoryOrSubcategory(category.getName());

        assertEquals(2, productServiceModels.get().size());
    }

    @Test
    public void getAllProductByCategoryOrSubcategory_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.getAllProductByCategoryOrSubcategory("Invalid Name");
        });

        String expectedMessage = ErrorConstants.CATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void isCategoryOrItsSubCategoriesHasProduct_shouldReturnTrue_whenCategoryOrSubCategoryHaveProducts(){
        Category category = new Category();
        category.setName("Category");

        category.setProducts(Set.of(new CategoryProduct(), new CategoryProduct()));

        categoryRepository.save(category);

        assertTrue(categoryService.isCategoryOrItsSubCategoriesHasProduct("Category"));
    }

    @Test
    public void isCategoryOrItsSubCategoriesHasProduct_shouldReturnFalse_whenCategoryOrSubCategoryHaveNotProducts(){
        Category category = new Category();
        category.setName("Category");

        category.setProducts(new HashSet<>());
        category.setSubCategories(new HashSet<>());


        categoryRepository.save(category);

        assertFalse(categoryService.isCategoryOrItsSubCategoriesHasProduct("Category"));
    }

    @Test
    public void isCategoryOrItsSubCategoriesHasProduct_shouldThrowException_whenGiveInvalidCategoryName(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            categoryService.isCategoryOrItsSubCategoriesHasProduct("Invalid Name");
        });

        String expectedMessage = ErrorConstants.CATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
