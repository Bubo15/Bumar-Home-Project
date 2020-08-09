package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Service;
import project.bumar.data.repositories.ServiceRepository;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.service.ServiceServiceModel;
import project.bumar.services.services.FileUploaderService;
import project.bumar.services.services.ServiceService;
import project.bumar.services.services.impl.ServiceServiceImpl;
import project.bumar.web.models.bindingModels.service.ServiceCreateBindingModel;
import project.bumar.web.models.bindingModels.service.ServiceEditBindingModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceServiceTest extends BaseTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @Mock
    private FileUploaderService fileUploaderService;

    @Mock
    private Publisher publisher;

    private ServiceService serviceService;

    @Before
    public void beforeEach(){
        serviceService = new ServiceServiceImpl(serviceRepository, fileUploaderService, new ModelMapper(), publisher);
    }

    @Test
    public void getAllServices_shouldReturnAllServices(){
        serviceRepository.saveAll(List.of(new Service()));

        List<ServiceServiceModel> serviceServiceModels = serviceService.getAllServices();

        assertEquals(1, serviceServiceModels.size());
    }

    @Test
    public void create_shouldCreateService_whenGiveValidData(){
        ServiceCreateBindingModel serviceCreateBindingModel = new ServiceCreateBindingModel();

        serviceCreateBindingModel.setName("Service");
        serviceCreateBindingModel.setDescription("description");

        Optional<ServiceServiceModel> serviceServiceModel = serviceService.create(serviceCreateBindingModel, null);

        assertEquals(serviceServiceModel.get().getName(), serviceCreateBindingModel.getName());
        assertEquals(serviceServiceModel.get().getDescription(), serviceCreateBindingModel.getDescription());
    }

    @Test
    public void create_shouldThrowException_whenGiveAlreadyExistingName(){
        Service service = new Service();
        service.setName("Service");

        serviceRepository.saveAndFlush(service);

        ServiceCreateBindingModel serviceCreateBindingModel = new ServiceCreateBindingModel();
        serviceCreateBindingModel.setName("Service");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            serviceService.create(serviceCreateBindingModel, null);
        });

        String expectedMessage = ErrorConstants.SERVICE_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void delete_shouldDeleteService_whenGiveValidData(){
        Service service = new Service();
        service.setName("Service");

        serviceRepository.saveAndFlush(service);

        Optional<ServiceServiceModel> serviceServiceModel = serviceService.delete(service.getId());

        assertEquals("Service", serviceServiceModel.get().getName());
        assertEquals(0, serviceRepository.count());
    }

    @Test
    public void delete_shouldThrowException_whenGiveNotExistingId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            serviceService.delete(-1);
        });

        String expectedMessage = ErrorConstants.SERVICE_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getServiceByName_shouldReturnService_whenServiceExist(){
        Service service = new Service();
        service.setName("Service");

        serviceRepository.saveAndFlush(service);

        Optional<ServiceServiceModel> serviceServiceModel = serviceService.getServiceByName("Service");

        assertEquals(service.getName(), serviceServiceModel.get().getName());
    }

    @Test
    public void getServiceByName_shouldThrowException_whenGiveNotExistingId(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            serviceService.getServiceByName("NotExistingName");
        });

        String expectedMessage = ErrorConstants.SERVICE_NAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldEditService_whenGiveValidData(){
        Service service = new Service();
        service.setName("Service");
        service.setDescription("Description");

        serviceRepository.saveAndFlush(service);

        ServiceEditBindingModel serviceEditBindingModel = new ServiceEditBindingModel();
        serviceEditBindingModel.setName("NewName");
        serviceEditBindingModel.setDescription("");

        Optional<ServiceServiceModel> serviceServiceModel = serviceService.edit(service.getName(), serviceEditBindingModel, null);

        assertEquals(serviceServiceModel.get().getName(), service.getName());
    }

    @Test
    public void edit_shouldThrowException_whenGiveAlreadyExistingService(){
        Service service = new Service();
        service.setName("Service");

        serviceRepository.saveAndFlush(service);

        ServiceEditBindingModel serviceEditBindingModel = new ServiceEditBindingModel();
        serviceEditBindingModel.setName("Service");

        BaseCustomException exception = assertThrows(AlreadyExistException.class, () -> {
            serviceService.edit(service.getName(), serviceEditBindingModel, null);
        });

        String expectedMessage = ErrorConstants.SERVICE_ALREADY_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldThrowException_whenGiveNotExistServiceToEdit(){
        ServiceEditBindingModel serviceEditBindingModel = new ServiceEditBindingModel();
        serviceEditBindingModel.setName("Service");

        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            serviceService.edit("NameNotFound", serviceEditBindingModel, null);
        });

        String expectedMessage = ErrorConstants.SERVICE_NAME_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
