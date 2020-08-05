package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.service.services.ProductService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.product.ProductCreateBindingModel;
import project.bumar.web.models.bindingModels.product.ProductEditBindingModel;
import project.bumar.web.models.responseModel.product.ProductDetailsResponseModel;
import project.bumar.web.models.responseModel.product.ProductResponseModel;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, Validator validator, ModelMapper modelMapper) {
        this.productService = productService;
        this.validator = validator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ProductDetailsResponseModel> details(@PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.productService.getProductDetailsById(id).orElseThrow(), ProductDetailsResponseModel.class));
    }


    @GetMapping("/new")
    public ResponseEntity<List<ProductResponseModel>> getNewProducts() {
        return ResponseEntity.ok(
                this.productService
                        .getAllNewProduct()
                        .stream()
                        .limit(3)
                        .map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductResponseModel.class))
                        .collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/category/{categoryName}/create")
    public ResponseEntity<ProductResponseModel> createCategoryProduct(@PathVariable String categoryName,
                                                                      @RequestPart ProductCreateBindingModel productCreateBindingModel,
                                                                      @RequestPart(name = "pictureFile", required = false) MultipartFile file,
                                                                      BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.modelMapper.map(
                this.productService.createCategoryProduct(productCreateBindingModel, file, categoryName).orElseThrow(), ProductResponseModel.class));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/category/{categoryName}/subcategory/{subcategoryName}/create")
    public ResponseEntity<ProductResponseModel> createSubcategoryProduct(@PathVariable String categoryName, @PathVariable String subcategoryName,
                                                                         @RequestPart ProductCreateBindingModel productCreateBindingModel,
                                                                         @RequestPart(name = "pictureFile", required = false) MultipartFile file,
                                                                         BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.modelMapper.map(
                this.productService.createSubcategoryProduct(productCreateBindingModel, file, categoryName, subcategoryName).orElseThrow(), ProductResponseModel.class));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public ResponseEntity<ProductDetailsResponseModel> edit(@PathVariable long id, @RequestPart("productEditBindingModel") ProductEditBindingModel productEditBindingModel,
                                                            @RequestPart(value = "pictureFile", required = false) MultipartFile file) {

        if (this.validator.isThereProductEditBindingModelErrors(productEditBindingModel)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.modelMapper.map(
                this.productService.edit(productEditBindingModel, id, file).orElseThrow(), ProductDetailsResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<ProductResponseModel> deleteCategoryProduct(@PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.productService.deleteCategoryProduct(id).orElseThrow(), ProductResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/subcategory/delete/{id}")
    public ResponseEntity<ProductResponseModel> deleteSubcategoryProduct(@PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.productService.deleteSubcategoryProduct(id).orElseThrow(), ProductResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}/delete/description/{key}")
    public ResponseEntity<ProductResponseModel> deleteDescriptionOfProductByKey(@PathVariable long id, @PathVariable String key) {
        return ResponseEntity
                .ok(this.modelMapper.map(this.productService.deleteDescriptionOfProductByKey(key, id).orElseThrow(), ProductResponseModel.class));
    }
}
