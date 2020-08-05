package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.service.services.BusinessService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.business.BusinessCreateBindingModel;
import project.bumar.web.models.bindingModels.business.BusinessEditBindingModel;
import project.bumar.web.models.responseModel.business.BusinessNameResponseModel;
import project.bumar.web.models.responseModel.business.BusinessResponseModel;
import project.bumar.web.models.responseModel.product.ProductResponseModel;

import javax.validation.Valid;
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
@RequestMapping("/business")
public class BusinessController {

    private final BusinessService businessService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public BusinessController(BusinessService businessService, ModelMapper modelMapper, Validator validator) {
        this.businessService = businessService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<BusinessResponseModel>>> getAllBusiness(Principal principal) {
        List<BusinessResponseModel> list = this.businessService
                .getAllBusiness()
                .stream()
                .map(businessServiceModel -> this.modelMapper.map(businessServiceModel, BusinessResponseModel.class))
                .peek(createBusinessProductLinks(principal))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.wrap(list));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseModel>> getAllProductsByBusiness(Principal principal, @PathVariable long id) {
        return ResponseEntity.ok(
                this.businessService
                        .getAllProductByBusiness(id)
                        .stream()
                        .limit(principal == null ? 5 : Integer.MAX_VALUE)
                        .map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductResponseModel.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/all/name")
    public ResponseEntity<List<BusinessNameResponseModel>> getAllBusinessName() {
        return ResponseEntity.ok(
                this.businessService
                        .getAllBusinessName()
                        .stream()
                        .map(businessNameServiceModel -> this.modelMapper.map(businessNameServiceModel, BusinessNameResponseModel.class))
                        .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/create")
    public ResponseEntity<BusinessResponseModel> create(@Valid @RequestPart("businessCreateBindingModel") BusinessCreateBindingModel businessCreateBindingModel,
                                                        @RequestPart(value = "pictureFile", required = false) MultipartFile file,
                                                        BindingResult result) throws URISyntaxException {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(new URI("/business/create"))
                .body(this.modelMapper.map(this.businessService.create(businessCreateBindingModel, file).orElseThrow(), BusinessResponseModel.class));

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<BusinessResponseModel> edit(@PathVariable long id, @RequestPart("businessEditBindingModel") BusinessEditBindingModel businessEditBindingModel,
                                                      @RequestPart(value = "pictureFile", required = false) MultipartFile file) {

        if (validator.isThereBusinessEditBindingModelErrors(businessEditBindingModel, id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.modelMapper.map(this.businessService.edit(id, businessEditBindingModel, file).orElseThrow(), BusinessResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BusinessResponseModel> delete(@PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.businessService.delete(id).orElseThrow(), BusinessResponseModel.class));
    }

    private Consumer<BusinessResponseModel> createBusinessProductLinks(Principal principal) {
        return b -> {
            List<Link> result = new ArrayList<>();

            if (this.businessService.isBusinessHasProduct(b.getId())) {
                Link allProductByBusiness = linkTo(methodOn(BusinessController.class)
                        .getAllProductsByBusiness(principal, b.getId())).withRel("business-product");

                result.add(allProductByBusiness);
            }

            Link deleteBusinessById = linkTo(methodOn(BusinessController.class)
                    .delete(b.getId())).withRel("business-delete");

            result.add(deleteBusinessById);

            b.add(result);
        };
    }
}
