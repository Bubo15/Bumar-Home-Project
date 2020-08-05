package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bumar.service.services.CommentService;
import project.bumar.web.models.bindingModels.comment.CommentBindingModel;
import project.bumar.web.models.responseModel.comment.CommentResponseModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentResponseModel>> getAll() {
        return ResponseEntity.ok(
                this
                        .commentService
                        .getAllComment()
                        .stream()
                        .map(commentServiceModel -> this.modelMapper.map(commentServiceModel, CommentResponseModel.class))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/create")
    public ResponseEntity<CommentResponseModel> create(@RequestBody CommentBindingModel commentBindingModel, Principal principal) throws URISyntaxException {
        return ResponseEntity.created(new URI("/comment/create"))
                .body(this.modelMapper.map(this.commentService.create(commentBindingModel, principal.getName()), CommentResponseModel.class));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<CommentResponseModel> edit(@RequestBody CommentBindingModel commentBindingModel, @PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.commentService.edit(commentBindingModel, id), CommentResponseModel.class));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommentResponseModel> edit(@PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.commentService.delete(id), CommentResponseModel.class));
    }
}