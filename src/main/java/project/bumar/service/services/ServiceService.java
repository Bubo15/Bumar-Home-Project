package project.bumar.service.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.service.models.service.ServiceServiceModel;
import project.bumar.web.models.bindingModels.service.ServiceCreateBindingModel;
import project.bumar.web.models.bindingModels.service.ServiceEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface ServiceService {

    List<ServiceServiceModel> getAllServices();

    @CacheEvict(cacheNames = "services", allEntries = true)
    void deleteServicesCache();

    Optional<ServiceServiceModel> create(ServiceCreateBindingModel serviceCreateBindingModel, MultipartFile file);

    Optional<ServiceServiceModel> delete(long id);

    Optional<ServiceServiceModel> getServiceByName(String name);

    Optional<ServiceServiceModel> edit(String name, ServiceEditBindingModel serviceEditBindingModel, MultipartFile file);
}
