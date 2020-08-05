package project.bumar.service.services;

import project.bumar.data.entities.Category;
import project.bumar.service.models.category.CategoryCreateServiceModel;
import project.bumar.service.models.category.CategoryServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.web.models.bindingModels.category.CategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.category.CategoryEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryServiceModel> getAllCategories();

    Optional<CategoryCreateServiceModel> create(CategoryCreateBindingModel category);

    Optional<CategoryServiceModel> delete(String name);

    Optional<CategoryServiceModel> edit(String name, CategoryEditBindingModel categoryEditBindingModel);

    CategoryServiceModel getCategoryByName(String categoryName);

    void saveCategory(Category category);

    Optional<List<ProductServiceModel>> getAllProductByCategoryOrSubcategory(String name);

    boolean isCategoryHasProduct(String name);
}
