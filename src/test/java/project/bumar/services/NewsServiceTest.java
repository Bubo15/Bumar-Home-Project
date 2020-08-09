package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.News;
import project.bumar.data.repositories.NewsRepository;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.news.NewsServiceModel;
import project.bumar.services.services.FileUploaderService;
import project.bumar.services.services.NewsService;
import project.bumar.services.services.impl.NewsServiceImpl;
import project.bumar.web.models.bindingModels.news.NewsCreateBindingModel;
import project.bumar.web.models.bindingModels.news.NewsEditBindingModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class NewsServiceTest extends BaseTest {

    @Autowired
    private NewsRepository newsRepository;

    @MockBean
    private FileUploaderService fileUploaderService;

    @MockBean
    private Publisher publisher;

    private NewsService newsService;

    @Before
    public void beforeEach(){
        newsService = new NewsServiceImpl(newsRepository, fileUploaderService, publisher, new ModelMapper());
    }

    @Test
    public void getAll_shouldReturnAllNews(){
        newsRepository.saveAll(List.of(new News(), new News()));

        List<NewsServiceModel> newsServiceModels = newsService.getAll();

        assertEquals(2, newsServiceModels.size());
    }

    @Test
    public void create_shouldCreateNews_whenGiveValidData(){
        NewsCreateBindingModel newsCreateBindingModel = new NewsCreateBindingModel();
        newsCreateBindingModel.setTitle("Title");
        newsCreateBindingModel.setText("TextTextText");

        NewsServiceModel newsServiceModel = newsService.create(newsCreateBindingModel, null);

        assertEquals(newsCreateBindingModel.getText(), newsServiceModel.getText());
        assertEquals(newsCreateBindingModel.getTitle(), newsServiceModel.getTitle());
    }

    @Test
    public void delete_shouldDeleteNews_whenGiveValidData(){
        News news = new News();
        news.setTitle("Title");
        news.setText("TextTextText");

        newsRepository.saveAndFlush(news);

        Optional<NewsServiceModel> newsServiceModel = newsService.delete(news.getId());

        assertEquals(newsServiceModel.get().getTitle(), news.getTitle());
        assertEquals(newsServiceModel.get().getText(), news.getText());
    }

    @Test
    public void delete_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            newsService.delete(-1);
        });

        String expectedMessage = ErrorConstants.NEWS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void edit_shouldEditNews_whenGiveValidData(){
        News news = new News();
        news.setTitle("Title");
        news.setText("TextTextText");

        newsRepository.saveAndFlush(news);

        NewsEditBindingModel newseditBindingModel = new NewsEditBindingModel();
        newseditBindingModel.setTitle("TitleTitleTitle");
        newseditBindingModel.setText("Text");

        newsService.edit(newseditBindingModel, news.getId(), null);

        assertEquals(news.getTitle(), newseditBindingModel.getTitle());
        assertEquals(news.getText(), newseditBindingModel.getText());
    }

    @Test
    public void edit_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            newsService.edit(new NewsEditBindingModel(), -1, null);
        });

        String expectedMessage = ErrorConstants.NEWS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getNewsById_shouldReturnNews_whenGiveValidData(){
        News news = new News();
        news.setTitle("Title");
        news.setText("TextTextText");

        newsRepository.saveAndFlush(news);

        NewsServiceModel newsServiceModel = newsService.getNewsById(news.getId());

        assertEquals(newsServiceModel.getTitle(), news.getTitle());
        assertEquals(newsServiceModel.getText(), news.getText());
    }

    @Test
    public void getNewsById_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            newsService.getNewsById(-1);
        });

        String expectedMessage = ErrorConstants.NEWS_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
