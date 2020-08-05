package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.service.services.ServiceService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.service.ServiceCreateBindingModel;
import project.bumar.web.models.bindingModels.service.ServiceEditBindingModel;
import project.bumar.web.models.responseModel.service.ServiceResponseModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service")
@CrossOrigin(origins = {"http://localhost:3000"})
public class ServiceController {

    private final ServiceService serviceService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public ServiceController(ServiceService serviceService, ModelMapper modelMapper, Validator validator) {
        this.serviceService = serviceService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceResponseModel>> getAllService() {
        return ResponseEntity.ok(this.serviceService
                .getAllServices()
                .stream()
                .map(serviceServiceModel -> this.modelMapper.map(serviceServiceModel, ServiceResponseModel.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ServiceResponseModel> getServiceByName(@PathVariable String name) {
        return ResponseEntity.ok(this.modelMapper.map(this.serviceService.getServiceByName(name).orElseThrow(), ServiceResponseModel.class));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/create")
    public ResponseEntity<ServiceResponseModel> createService(@Valid @RequestPart("serviceCreateBindingModel") ServiceCreateBindingModel serviceCreateBindingModel,
                                                              @RequestPart(value = "pictureFile", required = false) MultipartFile file,
                                                              BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(URI.create("/news/create"))
                .body(this.modelMapper.map(this.serviceService.create(serviceCreateBindingModel, file).orElseThrow(), ServiceResponseModel.class));

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/edit/{name}")
    public ResponseEntity<ServiceResponseModel> editService(@PathVariable String name,
                                                            @RequestPart("serviceEditBindingModel") ServiceEditBindingModel serviceEditBindingModel,
                                                            @RequestPart(value = "pictureFile", required = false) MultipartFile file) {

        if (this.validator.isThereServiceEditBindingModelErrors(serviceEditBindingModel)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.modelMapper.map(this.serviceService.edit(name, serviceEditBindingModel, file).orElseThrow(), ServiceResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ServiceResponseModel> deleteService(@PathVariable long id) {
        return ResponseEntity.ok().body(this.modelMapper.map(this.serviceService.delete(id).orElseThrow(), ServiceResponseModel.class));
    }

}
