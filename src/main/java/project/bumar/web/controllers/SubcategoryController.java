package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bumar.service.services.CategoryService;
import project.bumar.service.services.SubcategoryService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.subcategory.SubcategoryEditBindingModel;
import project.bumar.web.models.responseModel.category.CategoryNameResponseModel;
import project.bumar.web.models.responseModel.product.ProductResponseModel;
import project.bumar.web.models.responseModel.subcategory.SubCategoryResponseModel;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subcategory")
@CrossOrigin(origins = {"http://localhost:3000"})
public class SubcategoryController {

    private final SubcategoryService subcategoryService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public SubcategoryController(SubcategoryService subcategoryService, CategoryService categoryService, ModelMapper modelMapper, Validator validator) {
        this.subcategoryService = subcategoryService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @GetMapping("/all/category/name")
    public ResponseEntity<List<CategoryNameResponseModel>> getAllCategoryNames() {
        return ResponseEntity.ok(this.categoryService
                .getAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryNameResponseModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{name}/category/{categoryName}/products")
    public ResponseEntity<List<ProductResponseModel>> getAllProductsBySubcategory(Principal principal,
                                                                                  @PathVariable String name,
                                                                                  @PathVariable String categoryName) {
        return ResponseEntity.ok(
                this.subcategoryService
                        .getAllProductBySubcategory(name, categoryName).orElseThrow()
                        .stream()
                        .limit(principal == null ? 5 : Integer.MAX_VALUE)
                        .map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductResponseModel.class))
                        .collect(Collectors.toList())
        );

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<SubCategoryResponseModel> create(@Valid @RequestBody SubcategoryCreateBindingModel subcategoryCreateBindingModel,
                                    BindingResult result) throws URISyntaxException {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(new URI("/subcategory/create"))
                .body(this.modelMapper.map(this.subcategoryService.create(subcategoryCreateBindingModel).orElseThrow(), SubCategoryResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{category}/{subcategory}")
    public ResponseEntity<SubCategoryResponseModel> edit(@PathVariable String category, @PathVariable String subcategory,
                                  @RequestBody SubcategoryEditBindingModel subcategoryEditBindingModel) {

        if (this.validator.isThereSubCategoryEditBindingModelErrors(subcategoryEditBindingModel)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .ok(this.modelMapper.map(this.subcategoryService.edit(subcategory, subcategoryEditBindingModel, category).orElseThrow(), SubCategoryResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<SubCategoryResponseModel> delete(@PathVariable String name) {
        return ResponseEntity.ok(this.modelMapper.map(this.subcategoryService.delete(name).orElseThrow(), SubCategoryResponseModel.class));
    }
}
