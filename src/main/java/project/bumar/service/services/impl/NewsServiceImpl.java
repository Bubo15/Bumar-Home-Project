package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.constant.ErrorConstants;
import project.bumar.constant.FileUploaderConstants;
import project.bumar.constant.NewsConstant;
import project.bumar.data.entities.News;
import project.bumar.data.entities.Picture;
import project.bumar.data.repositories.NewsRepository;
import project.bumar.event.events.file.DeleteImageEvent;
import project.bumar.event.events.news.NewsEvictCacheEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.news.NewsServiceModel;
import project.bumar.service.services.FileUploaderService;
import project.bumar.service.services.NewsService;
import project.bumar.web.models.bindingModels.news.NewsCreateBindingModel;
import project.bumar.web.models.bindingModels.news.NewsEditBindingModel;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final FileUploaderService fileUploaderService;
    private final Publisher publisher;
    private final ModelMapper modelMapper;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, FileUploaderService fileUploaderService, Publisher publisher, ModelMapper modelMapper) {
        this.newsRepository = newsRepository;
        this.fileUploaderService = fileUploaderService;
        this.publisher = publisher;
        this.modelMapper = modelMapper;
    }

    @Override
    @Cacheable("news")
    public List<NewsServiceModel> getAll() {
        return this.newsRepository
                .findAll()
                .stream()
                .map(news -> this.modelMapper.map(news, NewsServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = "news", allEntries = true)
    public void deleteNewsCache() {}


    @Override
    public NewsServiceModel create(NewsCreateBindingModel newsCreateBindingModel, MultipartFile file) {
        News news = this.modelMapper.map(newsCreateBindingModel, News.class);

        this.newsRepository.save(news);

        Picture picture = new Picture();

        if (file == null) {
            picture.setUrl(NewsConstant.DEFAULT_PICTURE);

            news.setPicture(picture);
        } else {
            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.NEWS_FOLDER_NAME, news.getId(), file);
            picture.setUrl(uploadedFileUrl);

            news.setPicture(picture);
        }

        this.newsRepository.saveAndFlush(news);

        this.publisher.publishEvent(new NewsEvictCacheEvent(this));

        return this.modelMapper.map(news, NewsServiceModel.class);
    }

    @Override
    @Transactional
    public Optional<NewsServiceModel> delete(long id) {

        NewsServiceModel news = this.modelMapper.map(this.getNativeNewsById(id), NewsServiceModel.class);

        this.newsRepository.deleteById(id);

        this.publisher.publishEvent(new NewsEvictCacheEvent(this));
        this.publisher.publishEvent(new DeleteImageEvent(this, news.getId()));

        return Optional.of(news);
    }

    @Override
    public NewsServiceModel edit(NewsEditBindingModel newseditBindingModel, long id, MultipartFile file) {
        News news = this.getNativeNewsById(id);

        this.newsRepository.saveAndFlush(setData(newseditBindingModel, news, file));

        this.publisher.publishEvent(new NewsEvictCacheEvent(this));

        return this.modelMapper.map(news, NewsServiceModel.class);
    }

    @Override
    public NewsServiceModel getNewsById(long id) {
        return this.modelMapper.map(this.getNativeNewsById(id), NewsServiceModel.class);
    }

    private News setData(NewsEditBindingModel newseditBindingModel, News news, MultipartFile file){
        if (!newseditBindingModel.getTitle().isEmpty()){
            news.setTitle(newseditBindingModel.getTitle());
        }

        if (!newseditBindingModel.getText().isEmpty()){
            news.setText(newseditBindingModel.getText());
        }

        if (file != null){
            Picture picture = new Picture();

            this.publisher.publishEvent(new DeleteImageEvent(this, news.getId()));

            String uploadedFileUrl = this.fileUploaderService.getUploadedFileUrl(FileUploaderConstants.NEWS_FOLDER_NAME, news.getId(), file);
            picture.setUrl(uploadedFileUrl);

            news.setPicture(picture);
        }
        return news;
    }

    private News getNativeNewsById(long id){
        return this.newsRepository
                .getNewsById(id)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.NEWS_ID_NOT_FOUND));
    }
}
