package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bumar.service.services.CategoryService;
import project.bumar.web.models.bindingModels.category.CategoryCreateBindingModel;
import project.bumar.web.models.bindingModels.category.CategoryEditBindingModel;
import project.bumar.web.models.responseModel.category.CategoryCreateResponseModel;
import project.bumar.web.models.responseModel.category.CategoryResponseModel;
import project.bumar.web.models.responseModel.product.ProductResponseModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseModel>> getCategories(Principal principal) {
        return ResponseEntity.ok(this.categoryService
                .getAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryResponseModel.class))
                .peek(createCategoryProductLinks(principal))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{name}/products")
    public ResponseEntity<List<ProductResponseModel>> getAllProductsByCategory(Principal principal, @PathVariable String name) {
        return ResponseEntity.ok(
                this.categoryService
                        .getAllProductByCategoryOrSubcategory(name).orElseThrow()
                        .stream()
                        .limit(principal == null ? 5 : Integer.MAX_VALUE)
                        .map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductResponseModel.class))
                        .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreateBindingModel categoryBinding) throws URISyntaxException {
        return ResponseEntity.created(new URI("/category/create"))
                .body(this.modelMapper.map(this.categoryService.create(categoryBinding).orElseThrow(), CategoryCreateResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{name}")
    public ResponseEntity<?> editCategory(@PathVariable String name, @RequestBody CategoryEditBindingModel categoryEditBindingModel) {
        return ResponseEntity
                .ok(this.modelMapper.map(this.categoryService.edit(name, categoryEditBindingModel).orElseThrow(), CategoryCreateResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<CategoryResponseModel> deleteCategory(@PathVariable String name) {
        return ResponseEntity.ok(this.modelMapper.map(this.categoryService.delete(name).orElseThrow(), CategoryResponseModel.class));
    }

    private Consumer<CategoryResponseModel> createCategoryProductLinks(Principal principal) {
        return c -> {
            List<Link> result = new ArrayList<>();

            if (this.categoryService.isCategoryHasProduct(c.getName())) {
                Link allProductByCategory = linkTo(methodOn(CategoryController.class)
                        .getAllProductsByCategory(principal, c.getName())).withRel("category-product");

                result.add(allProductByCategory);
            }

            c.add(result);
        };
    }
}
