package project.bumar.service.services;

import project.bumar.data.entities.SubCategory;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.service.models.subcategory.SubcategoryServiceModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface SubcategoryService {

    Optional<SubcategoryServiceModel> create(SubcategoryCreateBindingModel subcategoryCreateBindingModel);

    Optional<SubcategoryServiceModel> edit(String name, SubcategoryEditBindingModel subcategoryEditBindingModel, String currentCategory);

    Optional<SubcategoryServiceModel> delete(String name);

    void saveSubcategory(SubCategory subCategory);

    Optional<List<ProductServiceModel>> getAllProductBySubcategory(String name, String categoryName);

    SubcategoryServiceModel getSubcategoryByNameAndCategory(String subcategoryName, String categoryName);
}
