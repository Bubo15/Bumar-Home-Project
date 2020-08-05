package project.bumar.service.services;

import org.springframework.web.multipart.MultipartFile;
import project.bumar.service.models.news.NewsServiceModel;
import project.bumar.web.models.bindingModels.news.NewsCreateBindingModel;
import project.bumar.web.models.bindingModels.news.NewsEditBindingModel;

import java.util.List;
import java.util.Optional;

public interface NewsService {

    NewsServiceModel create(NewsCreateBindingModel newsCreateBindingModel, MultipartFile file);

    List<NewsServiceModel> getAll();

    Optional<NewsServiceModel> delete(long id);

    NewsServiceModel edit(NewsEditBindingModel newseditBindingModel, long id, MultipartFile file);

    NewsServiceModel getNewsById(long id);

    void deleteNewsCache();
}
