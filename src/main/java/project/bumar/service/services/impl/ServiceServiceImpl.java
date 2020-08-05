package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.constant.ErrorConstants;
import project.bumar.constant.FileUploaderConstants;
import project.bumar.constant.ServiceConstant;
import project.bumar.data.entities.Picture;
import project.bumar.data.repositories.ServiceRepository;
import project.bumar.event.events.file.DeleteImageEvent;
import project.bumar.event.events.service.ServiceEvictCacheEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.service.ServiceServiceModel;
import project.bumar.service.services.FileUploaderService;
import project.bumar.service.services.ServiceService;
import project.bumar.web.models.bindingModels.service.ServiceCreateBindingModel;
import project.bumar.web.models.bindingModels.service.ServiceEditBindingModel;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final FileUploaderService fileUploaderService;
    private final ModelMapper modelMapper;
    private final Publisher publisher;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository, FileUploaderService fileUploaderService, ModelMapper modelMapper, Publisher publisher) {
        this.serviceRepository = serviceRepository;
        this.fileUploaderService = fileUploaderService;
        this.modelMapper = modelMapper;
        this.publisher = publisher;
    }

    @Override
    @Cacheable("services")
    public List<ServiceServiceModel> getAllServices() {
        return this.serviceRepository
                .findAll()
                .stream()
                .map(service -> this.modelMapper.map(service, ServiceServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = "services", allEntries = true)
    public void deleteServicesCache() { }

    @Override
    public Optional<ServiceServiceModel> create(ServiceCreateBindingModel serviceCreateBindingModel, MultipartFile file) {

        if (this.serviceRepository.getServiceByName(serviceCreateBindingModel.getName()).isPresent()) {
            throw new AlreadyExistException(ErrorConstants.SERVICE_ALREADY_EXIST);
        }

        project.bumar.data.entities.Service service = this.modelMapper.map(serviceCreateBindingModel, project.bumar.data.entities.Service.class);

        this.serviceRepository.save(service);

        Picture picture = new Picture();

        if (file == null) {
            picture.setUrl(ServiceConstant.DEFAULT_PICTURE);
            service.setPicture(picture);
        } else {
            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.SERVICE_FOLDER_NAME, service.getId(), file);
            picture.setUrl(uploadedFileUrl);

            service.setPicture(picture);
        }

        this.serviceRepository.saveAndFlush(service);

        this.publisher.publishEvent(new ServiceEvictCacheEvent(this));

        return Optional.of(this.modelMapper.map(service, ServiceServiceModel.class));
    }

    @Override
    @Transactional
    public Optional<ServiceServiceModel> delete(long id) {

        ServiceServiceModel service = this.modelMapper.map(
                this.serviceRepository.getServiceById(id).orElseThrow(() -> new NotFoundException(ErrorConstants.SERVICE_ID_NOT_FOUND)),
                ServiceServiceModel.class);

        this.serviceRepository.deleteById(id);

        this.publisher.publishEvent(new ServiceEvictCacheEvent(this));
        this.publisher.publishEvent(new DeleteImageEvent(this, service.getId()));

        return Optional.of(service);
    }

    @Override
    public Optional<ServiceServiceModel> getServiceByName(String name) {
        project.bumar.data.entities.Service service =
                this.serviceRepository.getServiceByName(name).orElseThrow(() -> new NotFoundException(ErrorConstants.SERVICE_NAME_NOT_FOUND));

        return Optional.of(this.modelMapper.map(service, ServiceServiceModel.class));
    }

    @Override
    public Optional<ServiceServiceModel> edit(String name, ServiceEditBindingModel serviceEditBindingModel, MultipartFile file) {

        if (this.serviceRepository.getServiceByName(serviceEditBindingModel.getName()).isPresent()) {
            throw new AlreadyExistException(ErrorConstants.SERVICE_ALREADY_EXIST);
        }

        project.bumar.data.entities.Service service =
                this.serviceRepository.getServiceByName(name).orElseThrow(() -> new NotFoundException(ErrorConstants.SERVICE_NAME_NOT_FOUND));

        this.serviceRepository.saveAndFlush(setData(serviceEditBindingModel, service, file));

        this.publisher.publishEvent(new ServiceEvictCacheEvent(this));

        return Optional.of(this.modelMapper.map(service, ServiceServiceModel.class));
    }

    private project.bumar.data.entities.Service setData(ServiceEditBindingModel serviceCreateBindingModel, project.bumar.data.entities.Service service, MultipartFile file) {

        if (!serviceCreateBindingModel.getName().isEmpty()) {
            service.setName(serviceCreateBindingModel.getName());
        }

        if (!serviceCreateBindingModel.getDescription().isEmpty()) {
            service.setDescription(serviceCreateBindingModel.getDescription());
        }

        if (file != null) {
            Picture picture = new Picture();

            this.publisher.publishEvent(new DeleteImageEvent(this, service.getId()));

            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.SERVICE_FOLDER_NAME, service.getId(), file);
            picture.setUrl(uploadedFileUrl);

            service.setPicture(picture);
        }

        return service;
    }
}
