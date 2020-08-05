package project.bumar.validate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.bumar.service.services.BusinessService;
import project.bumar.service.services.NewsService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.business.BusinessEditBindingModel;
import project.bumar.web.models.bindingModels.news.NewsEditBindingModel;
import project.bumar.web.models.bindingModels.product.ProductEditBindingModel;
import project.bumar.web.models.bindingModels.service.ServiceEditBindingModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryEditBindingModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ValidatorImpl implements Validator {

    private final NewsService newsService;
    private final BusinessService businessService;

    @Autowired
    public ValidatorImpl(NewsService newsService, BusinessService businessService) {
        this.newsService = newsService;
        this.businessService = businessService;
    }

    @Override
    public boolean isThereNewsEditBindingModelErrors(NewsEditBindingModel newsEditBindingModel, long id) {
        if (this.newsService.getNewsById(id) == null) {
            return true;
        }

        if (isThereTitleErrors(newsEditBindingModel.getTitle())) {
            return true;
        }

        return isThereTextErrors(newsEditBindingModel.getText());
    }

    @Override
    public boolean isThereServiceEditBindingModelErrors(ServiceEditBindingModel serviceEditBindingModel) {
        if (isThereTitleErrors(serviceEditBindingModel.getName())) {
            return true;
        }

        return isThereTextErrors(serviceEditBindingModel.getDescription());
    }

    @Override
    public boolean isThereSubCategoryEditBindingModelErrors(SubcategoryEditBindingModel subcategoryEditBindingModel) {
        if (isThereTitleErrors(subcategoryEditBindingModel.getName())) {
            return true;
        }

        return isThereTitleErrors(subcategoryEditBindingModel.getCategoryName());
    }

    @Override
    public boolean isThereBusinessEditBindingModelErrors(BusinessEditBindingModel businessEditBindingModel, long id) {
        if (this.businessService.getBusinessById(id) == null) {
            return true;
        }

        return isThereTitleErrors(businessEditBindingModel.getName());
    }

    @Override
    public boolean isThereProductDescriptionErrors(List<Map<String, String>> descriptions) {

//        AtomicBoolean hasErrors = new AtomicBoolean(false);
//        descriptions.forEach(map -> {
//            map.forEach((key, value) -> {
//                if ((entry.getValue().isEmpty() && !entry.getKey().isEmpty()) || (!entry.getValue().isEmpty() && entry.getKey().isEmpty())){
//                    hasErrors.set(true);
//                }
//            });
//        });

        for (Map<String, String> description : descriptions) {
            for (Map.Entry<String, String> entry : description.entrySet()) {
                if ((entry.getValue().isEmpty() && !entry.getKey().isEmpty()) || (!entry.getValue().isEmpty() && entry.getKey().isEmpty())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isThereProductEditBindingModelErrors(ProductEditBindingModel productEditBindingModel) {
        if (isThereTitleErrors(productEditBindingModel.getName())){
            return true;
        }

        if (!productEditBindingModel.getMainDescription().isEmpty()){
            return productEditBindingModel.getMainDescription().length() < 4;
        }

        if (productEditBindingModel.getCountOfProduct() != 0){
            return productEditBindingModel.getCountOfProduct() < 0;
        }

        if (productEditBindingModel.getPrice() != 0){
            return productEditBindingModel.getPrice() < 0;
        }

        if (isThereProductDescriptionErrors(productEditBindingModel.getDescription())){
            return true;
        }

        return isThereTitleErrors(productEditBindingModel.getBusinessName());
    }

    @Override
    public boolean isThereOrderProductError(Set<Map<String, Integer>> products) {
        for (Map<String, Integer> product : products) {
            for (Map.Entry<String, Integer> entry : product.entrySet()) {
                if ((entry.getValue() == 0 && !entry.getKey().isEmpty()) || (entry.getValue() != 0 && entry.getKey().isEmpty())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isThereTitleErrors(String name) {
        if (!name.isEmpty()) {
            return name.length() < 3;
        }
        return false;
    }

    private boolean isThereTextErrors(String text) {
        if (!text.isEmpty()) {
            return text.length() < 10;
        }
        return false;
    }
}
