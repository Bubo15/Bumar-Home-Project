package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.constant.BusinessConstants;
import project.bumar.constant.ErrorConstants;
import project.bumar.constant.FileUploaderConstants;
import project.bumar.data.entities.Business;
import project.bumar.data.entities.Picture;
import project.bumar.data.repositories.BusinessRepository;
import project.bumar.event.events.business.BusinessEvictCacheEvent;
import project.bumar.event.events.file.DeleteImageEvent;
import project.bumar.event.events.order.DeleteOrderProductsEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.AlreadyExistException;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.business.BusinessNameServiceModel;
import project.bumar.service.models.business.BusinessServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.service.services.BusinessService;
import project.bumar.service.services.FileUploaderService;
import project.bumar.web.models.bindingModels.business.BusinessCreateBindingModel;
import project.bumar.web.models.bindingModels.business.BusinessEditBindingModel;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final FileUploaderService fileUploaderService;
    private final ModelMapper modelMapper;
    private final Publisher publisher;

    @Autowired
    public BusinessServiceImpl(ModelMapper modelMapper, BusinessRepository businessRepository, FileUploaderService fileUploaderService, Publisher publisher) {
        this.modelMapper = modelMapper;
        this.businessRepository = businessRepository;
        this.fileUploaderService = fileUploaderService;
        this.publisher = publisher;
    }

    @Override
    @Cacheable("businesses")
    public List<BusinessServiceModel> getAllBusiness() {
        return this.businessRepository
                .findAll()
                .stream()
                .map(business -> this.modelMapper.map(business, BusinessServiceModel.class))
                .collect(Collectors.toList());

    }

    @Override
    @CacheEvict(cacheNames = "businesses", allEntries = true)
    public void deleteBusinessesCache() { }

    @Override
    public List<ProductServiceModel> getAllProductByBusiness(long businessId) {
        return this
                .getNativeBusinessById(businessId)
                .getProducts()
                .stream()
                .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBusinessHasProduct(long id) {
        return !this
                .getNativeBusinessById(id)
                .getProducts()
                .isEmpty();
    }

    @Override
    public Optional<BusinessServiceModel> create(BusinessCreateBindingModel businessCreateBindingModel, MultipartFile file) {

        if (this.businessRepository.getBusinessByName(businessCreateBindingModel.getName()).isPresent()) {
            throw new AlreadyExistException(ErrorConstants.BUSINESS_ALREADY_EXIST);
        }

        Business business = this.modelMapper.map(businessCreateBindingModel, Business.class);
        business.setProducts(new HashSet<>());

        this.businessRepository.save(business);

        Picture picture = new Picture();

        if (file == null) {
            picture.setUrl(BusinessConstants.DEFAULT_PICTURE);

            business.setLogo(picture);
        } else {
            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.BUSINESS_FOLDER_NAME, business.getId(), file);
            picture.setUrl(uploadedFileUrl);

            business.setLogo(picture);
        }

        this.businessRepository.saveAndFlush(business);

        this.publisher.publishEvent(new BusinessEvictCacheEvent(this));

        return Optional.of(this.modelMapper.map(business, BusinessServiceModel.class));
    }

    @Override
    @Transactional
    public Optional<BusinessServiceModel> delete(long id) {

        Business business = this.getNativeBusinessById(id);

        this.publisher.publishEvent(new DeleteOrderProductsEvent(this, business.getProducts()));

        BusinessServiceModel businessServiceModel = this.modelMapper.map(business, BusinessServiceModel.class);
        this.businessRepository.deleteBusinessById(id);

        this.publisher.publishEvent(new BusinessEvictCacheEvent(this));
        this.publisher.publishEvent(new DeleteImageEvent(this, business.getId()));

        return Optional.of(businessServiceModel);
    }

    @Override
    public BusinessServiceModel getBusinessById(long id) {
        return this.modelMapper.map(this.getNativeBusinessById(id), BusinessServiceModel.class);
    }

    @Override
    public Optional<BusinessServiceModel> edit(long id, BusinessEditBindingModel businessEditBindingModel, MultipartFile file) {

        if (this.businessRepository.getBusinessByName(businessEditBindingModel.getName()).isPresent()) {
            throw new AlreadyExistException(ErrorConstants.BUSINESS_ALREADY_EXIST);
        }

        Business business = this.getNativeBusinessById(id);

        this.businessRepository.saveAndFlush(setData(businessEditBindingModel, business, file));

        this.publisher.publishEvent(new BusinessEvictCacheEvent(this));

        return Optional.of(this.modelMapper.map(business, BusinessServiceModel.class));
    }

    @Override
    public List<BusinessNameServiceModel> getAllBusinessName() {
        return this.businessRepository
                .findAll()
                .stream()
                .map(business -> this.modelMapper.map(business, BusinessNameServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public BusinessServiceModel getBusinessByName(String businessName) {
        return this.modelMapper.map(this.getNativeBusinessByName(businessName), BusinessServiceModel.class);
    }

    @Override
    public void saveBusiness(Business business) {
        this.businessRepository.saveAndFlush(business);
    }

    private Business setData(BusinessEditBindingModel businessEditBindingModel, Business business, MultipartFile file) {

        if (!businessEditBindingModel.getName().isEmpty()) {
            business.setName(businessEditBindingModel.getName());
        }

        if (file != null) {
            Picture picture = new Picture();

            this.publisher.publishEvent(new DeleteImageEvent(this, business.getId()));

            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.BUSINESS_FOLDER_NAME, business.getId(), file);
            picture.setUrl(uploadedFileUrl);

            business.setLogo(picture);
        }

        return business;
    }

    private Business getNativeBusinessByName(String name){
        return this.businessRepository.getBusinessByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.GIVEN_BUSINESS_NOT_FOUND));
    }

    private Business getNativeBusinessById(long id){
        return this.businessRepository.getBusinessById(id)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.BUSINESS_ID_NOT_FOUND));
    }
}
