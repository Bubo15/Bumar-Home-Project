package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Business;
import project.bumar.data.entities.Category;
import project.bumar.data.entities.SubCategory;
import project.bumar.data.entities.SubcategoryProduct;
import project.bumar.data.repositories.BusinessRepository;
import project.bumar.data.repositories.CategoryRepository;
import project.bumar.data.repositories.ProductRepository;
import project.bumar.data.repositories.SubcategoryRepository;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.product.ProductServiceModel;
import project.bumar.services.models.subcategory.SubcategoryServiceModel;
import project.bumar.services.services.CategoryService;
import project.bumar.services.services.SubcategoryService;
import project.bumar.services.services.impl.CategoryServiceImpl;
import project.bumar.services.services.impl.SubcategoryServiceImpl;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryEditBindingModel;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SubcategoryServiceTest extends BaseTest {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    private CategoryService categoryService;

    private SubcategoryService subcategoryService;

    @Mock
    private Publisher publisher;

    @Before
    public void beforeEach() {
        ModelMapper modelMapper = new ModelMapper();

        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper, publisher);
        subcategoryService = new SubcategoryServiceImpl(subcategoryRepository, categoryService, modelMapper, publisher);
    }

    @Test
    public void create_shouldCreateSubcategory_whenGiveValidData() {
        Category category = new Category();
        category.setName("category");
        category.setSubCategories(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        SubcategoryCreateBindingModel subcategoryCreateBindingModel = new SubcategoryCreateBindingModel();
        subcategoryCreateBindingModel.setName("subcategory");
        subcategoryCreateBindingModel.setCategoryName("category");

        Optional<SubcategoryServiceModel> subcategoryServiceModel = subcategoryService.create(subcategoryCreateBindingModel);

        assertEquals(subcategoryCreateBindingModel.getName(), subcategoryServiceModel.get().getName());
        assertEquals(1, categoryRepository.count());
    }

    @Test
    public void create_shouldThrowException_whenGiveNotExistCategory() {
        Category category = new Category();
        category.setName("category");
        category.setSubCategories(new HashSet<>() {{
            add(new SubCategory());
        }});

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(category);
        subCategory.setName("subcategory");

        subcategoryRepository.saveAndFlush(subCategory);

        SubcategoryCreateBindingModel subcategoryCreateBindingModel = new SubcategoryCreateBindingModel();
        subcategoryCreateBindingModel.setName("subcategory");
        subcategoryCreateBindingModel.setCategoryName("category");


        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            subcategoryService.create(subcategoryCreateBindingModel);
        });

        String expectedMessage = ErrorConstants.SUBCATEGORY_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_editSubcategory_whenGiveValidData() {
        Category category = new Category();
        category.setName("category");
        category.setSubCategories(new HashSet<>() {{ add(new SubCategory()); }});

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setCategory(category);
        subCategory.setName("subcategory");

        subcategoryRepository.saveAndFlush(subCategory);

        SubcategoryEditBindingModel subcategoryEditBindingModel = new SubcategoryEditBindingModel();
        subcategoryEditBindingModel.setName("newSubcategory");
        subcategoryEditBindingModel.setCategoryName("");

        Optional<SubcategoryServiceModel> subcategoryServiceModel =
                subcategoryService.edit("subcategory", subcategoryEditBindingModel, "category");

        assertEquals(subcategoryServiceModel.get().getName(), subCategory.getName());
        assertEquals(subcategoryServiceModel.get().getCategory().getName(), subCategory.getCategory().getName());
    }

    @Test
    public void edit_throwException_whenGiveInValidCategory() {
        SubcategoryEditBindingModel subcategoryEditBindingModel = new SubcategoryEditBindingModel();
        subcategoryEditBindingModel.setName("newSubcategory");
        subcategoryEditBindingModel.setCategoryName("newCategory");

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            subcategoryService.edit("subcategory", subcategoryEditBindingModel, "category");
        });

        String expectedMessage = ErrorConstants.CATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_throwException_whenGiveAlreadyExistSubCatInCategory() {
        SubCategory subCategory = new SubCategory();
        subCategory.setName("subcategory");

        subcategoryRepository.saveAndFlush(subCategory);

        Category category = new Category();
        category.setName("category");
        category.setSubCategories(Set.of(subCategory));

        categoryRepository.saveAndFlush(category);

        SubcategoryEditBindingModel subcategoryEditBindingModel = new SubcategoryEditBindingModel();
        subcategoryEditBindingModel.setName("subcategory");
        subcategoryEditBindingModel.setCategoryName("category");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            subcategoryService.edit("subcategory", subcategoryEditBindingModel, "category");
        });

        String expectedMessage = ErrorConstants.SUBCATEGORY_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_throwException_whenGiveNotExistCurrentCategory() {
        SubCategory subCategory = new SubCategory();
        subCategory.setName("subcategory");

        subcategoryRepository.saveAndFlush(subCategory);

        Category category = new Category();
        category.setName("category");
        category.setSubCategories(Set.of(subCategory));

        categoryRepository.saveAndFlush(category);

        SubcategoryEditBindingModel subcategoryEditBindingModel = new SubcategoryEditBindingModel();
        subcategoryEditBindingModel.setName("subcategory");
        subcategoryEditBindingModel.setCategoryName("");

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            subcategoryService.edit("subcategory", subcategoryEditBindingModel, "NotFound");
        });

        String expectedMessage = ErrorConstants.GIVEN_CATEGORY_OR_SUBCATEGORY_NAME_FROM_URL_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void delete_deleteSubCat_whenGiveValidData(){
        Business business = new Business();
        business.setProducts(new HashSet<>());

        businessRepository.saveAndFlush(business);

        SubcategoryProduct subcategoryProduct = new SubcategoryProduct();
        subcategoryProduct.setBusiness(business);

        productRepository.saveAndFlush(subcategoryProduct);

        Category category = new Category();
        category.setName("category");
        category.setSubCategories(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setName("subcategory");
        subCategory.setProducts(Set.of(subcategoryProduct));
        subCategory.setCategory(category);

        subcategoryRepository.saveAndFlush(subCategory);

        Optional<SubcategoryServiceModel> subcategoryServiceModel = subcategoryService.delete(subCategory.getName());

        assertEquals(0, subcategoryRepository.count());
        assertEquals(subCategory.getName(), subcategoryServiceModel.get().getName());
    }

    @Test
    public void delete_shouldThrowException_whenGiveNotExistSubCatName(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            subcategoryService.delete("subcategory");
        });

        String expectedMessage = ErrorConstants.SUBCATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllProductBySubcategory_shouldReturnAllProductsBySubcategory_whenGiveValidData(){
        Category category = new Category();
        category.setName("category");
        category.setSubCategories(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setName("subcategory");
        subCategory.setProducts(Set.of(new SubcategoryProduct(), new SubcategoryProduct()));
        subCategory.setCategory(category);

        subcategoryRepository.saveAndFlush(subCategory);

        Optional<List<ProductServiceModel>> optionalProductServiceModels = subcategoryService.getAllProductBySubcategory(subCategory.getName(), category.getName());

        assertEquals(2, optionalProductServiceModels.get().size());
    }

    @Test
    public void getAllProductBySubcategory_shouldThrowException_whenGiveNotExistSubCatName(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            subcategoryService.getAllProductBySubcategory("subcategory", "category");
        });

        String expectedMessage = ErrorConstants.SUBCATEGORY_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getSubcategoryByNameAndCategory_shouldReturnSubcategory_whenGiveValidData(){
        Category category = new Category();
        category.setName("category");
        category.setSubCategories(new HashSet<>());

        categoryRepository.saveAndFlush(category);

        SubCategory subCategory = new SubCategory();
        subCategory.setName("subcategory");
        subCategory.setProducts(Set.of(new SubcategoryProduct(), new SubcategoryProduct()));
        subCategory.setCategory(category);

        subcategoryRepository.saveAndFlush(subCategory);

        SubcategoryServiceModel subcategoryServiceModel = subcategoryService.getSubcategoryByNameAndCategory(subCategory.getName(), category.getName());

        assertEquals(subcategoryServiceModel.getName(), subCategory.getName());
        assertEquals(subcategoryServiceModel.getCategory().getName(), subCategory.getCategory().getName());
    }

    @Test
    public void getSubcategoryByNameAndCategory_shouldThrowException_whenGiveNotExistSubCatName(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            subcategoryService.getSubcategoryByNameAndCategory("subcategory", "category");
        });

        String expectedMessage = ErrorConstants.GIVEN_CATEGORY_OR_SUBCATEGORY_NAME_FROM_URL_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void save_shouldSaveSubcategory(){
        subcategoryService.saveSubcategory(new SubCategory());
        assertEquals(1, subcategoryRepository.count());
    }
}
