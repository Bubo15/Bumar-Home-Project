package project.bumar.event.listeners.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.bumar.event.events.news.NewsEvictCacheEvent;
import project.bumar.service.services.NewsService;

@Component
public class EvictCacheNewsListener{

    private final NewsService newsService;

    @Autowired
    public EvictCacheNewsListener(NewsService newsService) {
        this.newsService = newsService;
    }

    @EventListener(NewsEvictCacheEvent.class)
    public void evictCache(){
        this.newsService.deleteNewsCache();
    }
}
