package project.bumar.service.services;

import org.springframework.web.multipart.MultipartFile;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.service.models.product.ProductDetailsServiceModel;
import project.bumar.service.models.product.ProductServiceModel;
import project.bumar.web.models.bindingModels.product.ProductCreateBindingModel;
import project.bumar.web.models.bindingModels.product.ProductEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductServiceModel> createCategoryProduct(ProductCreateBindingModel productCreateBindingModel, MultipartFile file, String categoryName);

    Optional<ProductServiceModel> createSubcategoryProduct(ProductCreateBindingModel productCreateBindingModel, MultipartFile file,String categoryName, String subcategoryName);

    Optional<ProductServiceModel> deleteCategoryProduct(long id);

    Optional<ProductServiceModel> deleteSubcategoryProduct(long id);

    Optional<ProductDetailsServiceModel> getProductDetailsById(long id);

    Optional<ProductDetailsServiceModel> edit(ProductEditBindingModel productEditBindingModel, long id, MultipartFile file);

    BaseProduct getProductById(long id);

    Optional<ProductDetailsServiceModel> deleteDescriptionOfProductByKey(String key, long productId);

    void saveProduct(BaseProduct product);

    List<ProductServiceModel> getAllNewProduct();
}
