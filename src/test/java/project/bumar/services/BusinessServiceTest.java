package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Business;
import project.bumar.data.entities.CategoryProduct;
import project.bumar.data.entities.Picture;
import project.bumar.data.entities.SubcategoryProduct;
import project.bumar.data.repositories.BusinessRepository;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.business.BusinessNameServiceModel;
import project.bumar.services.models.business.BusinessServiceModel;
import project.bumar.services.models.product.ProductServiceModel;
import project.bumar.services.services.BusinessService;
import project.bumar.services.services.FileUploaderService;
import project.bumar.services.services.impl.BusinessServiceImpl;
import project.bumar.web.models.bindingModels.business.BusinessCreateBindingModel;
import project.bumar.web.models.bindingModels.business.BusinessEditBindingModel;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BusinessServiceTest extends BaseTest {

    @MockBean
    private BusinessRepository businessRepository;

    @Mock
    private FileUploaderService fileUploaderService;

    private ModelMapper modelMapper;

    @Mock
    private Publisher publisher;

    private BusinessService businessService;

    @Before
    public void setUp() {
        modelMapper = new ModelMapper();
        businessService = new BusinessServiceImpl(modelMapper, businessRepository, fileUploaderService, publisher);
    }

    @Test
    public void getAllBusiness_shouldReturnAllBusinesses() {

        Business business = new Business();
        business.setId(1);
        business.setName("Business");
        business.setProducts(new HashSet<>());
        business.setLogo(new Picture());

        when(businessRepository.findAll()).thenReturn(List.of(business));

        List<BusinessServiceModel> businessServiceModels = businessService.getAllBusiness();

        BusinessServiceModel businessServiceModel = businessServiceModels.get(0);

        Assertions.assertEquals(1, businessServiceModels.size());
        assertEquals(businessServiceModel.getName(), business.getName());
        assertEquals(businessServiceModel.getId(), business.getId());
    }

    @Test
    public void getBusinessProducts_shouldReturnAllProductsByBusiness(){

        Business business = new Business();
        business.setId(1);
        business.setName("Business");
        business.setProducts(Set.of(new CategoryProduct(), new SubcategoryProduct()));
        business.setLogo(new Picture());

        when(businessRepository.getBusinessById(1)).thenReturn(Optional.of(business));

        List<ProductServiceModel> productServiceModels = businessService.getAllProductByBusiness(1);

        assertEquals(2, productServiceModels.size());
    }

    @Test
    public void getBusinessProducts_throwException_ifGiveInvalidId(){

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
             businessService.getAllProductByBusiness(-1);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void isBusinessHasProduct_return_true_ifHasProducts(){
        Business business = new Business();
        business.setId(1);
        business.setName("Business");
        business.setProducts(Set.of(new CategoryProduct(), new SubcategoryProduct()));
        business.setLogo(new Picture());

        when(businessRepository.getBusinessById(1)).thenReturn(Optional.of(business));

        assertTrue(businessService.isBusinessHasProduct(1));
    }

    @Test
    public void isBusinessHasProduct_return_false_ifHasNotProducts(){
        Business business = new Business();
        business.setId(1);
        business.setName("Business");
        business.setProducts(Set.of());
        business.setLogo(new Picture());

        when(businessRepository.getBusinessById(1)).thenReturn(Optional.of(business));

        assertFalse(businessService.isBusinessHasProduct(1));
    }

    @Test
    public void isBusinessHasProduct_throwException_ifGiveInvalidId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            businessService.isBusinessHasProduct(-1);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void create_shouldCreateBusiness_whenGivenValidInputData_and_businessNotExist(){
        BusinessCreateBindingModel businessCreateBindingModel = new BusinessCreateBindingModel();
        businessCreateBindingModel.setName("Business");

        Optional<BusinessServiceModel> businessServiceModel = businessService.create(businessCreateBindingModel, null);

        assertEquals("Business", businessServiceModel.get().getName());
    }

    @Test
    public void create_shouldNotCreateBusiness_whenBusinessAlreadyExist(){
        BusinessCreateBindingModel businessCreateBindingModel = new BusinessCreateBindingModel();
        businessCreateBindingModel.setName("Business");

        Business business = new Business();
        business.setName("Business");

        when(businessRepository.getBusinessByName("Business")).thenReturn(Optional.of(business));

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            businessService.create(businessCreateBindingModel, null);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void delete_shouldDeleteBusiness_ifGivenValidId(){
        Business business = new Business();
        business.setId(2);
        business.setName("Business");

        when(businessRepository.getBusinessById(2)).thenReturn(Optional.of(business));

        Optional<BusinessServiceModel> businessServiceModel = businessService.delete(2);

        assertEquals("Business", businessServiceModel.get().getName());
        assertEquals(2, businessServiceModel.get().getId());

        verify(businessRepository, times(1)).deleteBusinessById(eq(2L));
    }

    @Test
    public void delete_shouldNotDeleteBusiness_ifGivenInValidId(){

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            businessService.delete(1000000);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getBusinessById_shouldReturnBusiness_ifGivenValidId(){
        Business business = new Business();
        business.setId(2);
        business.setName("Business");

        when(businessRepository.getBusinessById(2)).thenReturn(Optional.of(business));

        BusinessServiceModel businessServiceModel = businessService.getBusinessById(2);

        assertEquals("Business", businessServiceModel.getName());
        assertEquals(2, businessServiceModel.getId());
    }

    @Test
    public void getBusinessById_shouldReturnBusiness_ifGivenInValidId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            businessService.getBusinessById(1000000);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldEditBusiness_ifGiveValidData_and_notAlreadyExistBusiness(){
        Business businessAlreadyInDB = new Business();
        businessAlreadyInDB.setId(2);
        businessAlreadyInDB.setName("Business");

        BusinessEditBindingModel businessEditBindingModel = new BusinessEditBindingModel();
        businessEditBindingModel.setName("Business2");

        when(businessRepository.getBusinessByName(businessEditBindingModel.getName())).thenReturn(Optional.empty());
        when(businessRepository.getBusinessById(2)).thenReturn(Optional.of(businessAlreadyInDB));

        Optional<BusinessServiceModel> businessServiceModel = businessService.edit(2, businessEditBindingModel, null);

        assertEquals(businessServiceModel.get().getName(), "Business2");
    }

    @Test
    public void edit_shouldNotEditBusiness_ifGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            businessService.edit(2, new BusinessEditBindingModel(), null);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldNotEditBusiness_ifGiveValidData_and_alreadyExistBusiness(){
        BusinessEditBindingModel businessEditBindingModel = new BusinessEditBindingModel();
        businessEditBindingModel.setName("Business");

        when(businessRepository.getBusinessByName(businessEditBindingModel.getName())).thenReturn(Optional.of(new Business()));

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            businessService.edit(1, businessEditBindingModel, null);
        });

        String expectedMessage = ErrorConstants.BUSINESS_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllBusinessName_shouldReturnAllBusinessNames(){

        when(businessRepository.findAll()).thenReturn(List.of(new Business(), new Business()));

        List<BusinessNameServiceModel> businessNameServiceModels = businessService.getAllBusinessName();

        assertEquals(2, businessNameServiceModels.size());
    }

    @Test
    public void getBusinessByName_shouldReturnBusiness_ifGiveValidName(){
        Business businessAlreadyInDB = new Business();
        businessAlreadyInDB.setId(1);
        businessAlreadyInDB.setName("Business");

        when(businessRepository.getBusinessByName("Business")).thenReturn(Optional.of(businessAlreadyInDB));

        BusinessServiceModel businessServiceModel = businessService.getBusinessByName("Business");

        assertEquals("Business", businessServiceModel.getName());
    }

    @Test
    public void getBusinessByName_shouldNotReturnBusiness_ifGiveInValidName(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            businessService.getBusinessByName("Business");
        });

        String expectedMessage = ErrorConstants.GIVEN_BUSINESS_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
