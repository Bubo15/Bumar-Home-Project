package project.bumar.service.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.data.entities.Business;
import project.bumar.service.models.business.BusinessNameServiceModel;
import project.bumar.service.models.business.BusinessServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.web.models.bindingModels.business.BusinessCreateBindingModel;
import project.bumar.web.models.bindingModels.business.BusinessEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface BusinessService {

    List<BusinessServiceModel> getAllBusiness();

    @CacheEvict(cacheNames = "businesses", allEntries = true)
    void deleteBusinessesCache();

    List<ProductServiceModel> getAllProductByBusiness(long businessId);

    boolean isBusinessHasProduct(long id);

    Optional<BusinessServiceModel> create(BusinessCreateBindingModel businessCreateBindingModel, MultipartFile file);

    Optional<BusinessServiceModel> delete(long id);

    BusinessServiceModel getBusinessById(long id);

    Optional<BusinessServiceModel> edit(long id, BusinessEditBindingModel businessEditBindingModel, MultipartFile file);

    List<BusinessNameServiceModel> getAllBusinessName();

    BusinessServiceModel getBusinessByName(String businessName);

    void saveBusiness(Business business);
}
