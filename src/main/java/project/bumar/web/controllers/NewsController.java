package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.service.services.NewsService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.news.NewsCreateBindingModel;
import project.bumar.web.models.bindingModels.news.NewsEditBindingModel;
import project.bumar.web.models.responseModel.news.NewsResponseModel;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
@CrossOrigin(origins = {"http://localhost:3000"})
public class NewsController {

    private final NewsService newsService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public NewsController(NewsService newsService, ModelMapper modelMapper, Validator validator) {
        this.newsService = newsService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @GetMapping("/all")
    public ResponseEntity<List<NewsResponseModel>> getAllNews() {
        return ResponseEntity.ok(this.newsService
                .getAll()
                .stream()
                .map(newsServiceModel -> this.modelMapper.map(newsServiceModel, NewsResponseModel.class))
                .collect(Collectors.toList()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/create")
    public ResponseEntity<NewsResponseModel> createNews(@Valid @RequestPart("newsCreateBindingModel") NewsCreateBindingModel newsCreateBindingModel,
                                                        @RequestPart(value = "pictureFile", required = false) MultipartFile file,
                                                        BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(URI.create("/news/create"))
                .body(this.modelMapper.map(this.newsService.create(newsCreateBindingModel, file), NewsResponseModel.class));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<NewsResponseModel> delete(@PathVariable long id) {
        return ResponseEntity.ok(this.modelMapper.map(this.newsService.delete(id).orElseThrow(), NewsResponseModel.class));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/edit/{id}")
    public ResponseEntity<NewsResponseModel> editNews(@PathVariable long id, @RequestPart("newsEditBindingModel") NewsEditBindingModel newsEditBindingModel,
                                                      @RequestPart(value = "pictureFile", required = false) MultipartFile file) {

        if (validator.isThereNewsEditBindingModelErrors(newsEditBindingModel, id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(this.modelMapper.map(this.newsService.edit(newsEditBindingModel, id, file), NewsResponseModel.class));
    }
}
