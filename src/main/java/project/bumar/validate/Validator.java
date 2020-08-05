package project.bumar.validate;

import project.bumar.web.models.bindingModels.business.BusinessEditBindingModel;
import project.bumar.web.models.bindingModels.news.NewsEditBindingModel;
import project.bumar.web.models.bindingModels.product.ProductEditBindingModel;
import project.bumar.web.models.bindingModels.service.ServiceEditBindingModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryEditBindingModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Validator {

    boolean isThereNewsEditBindingModelErrors(NewsEditBindingModel newsEditBindingModel, long id);

    boolean isThereServiceEditBindingModelErrors(ServiceEditBindingModel serviceEditBindingModel);

    boolean isThereSubCategoryEditBindingModelErrors(SubcategoryEditBindingModel subcategoryEditBindingModel);

    boolean isThereBusinessEditBindingModelErrors(BusinessEditBindingModel businessEditBindingModel, long id);

    boolean isThereProductDescriptionErrors(List<Map<String, String>> description);

    boolean isThereProductEditBindingModelErrors(ProductEditBindingModel productEditBindingModel);

    boolean isThereOrderProductError(Set<Map<String, Integer>> products);
}
