package project.bumar.event.listeners.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.bumar.event.events.business.BusinessEvictCacheEvent;
import project.bumar.service.services.BusinessService;

@Component
public class EvictCacheBusinessListener {

    private final BusinessService businessService;

    @Autowired
    public EvictCacheBusinessListener(BusinessService businessService) {
        this.businessService = businessService;
    }

    @EventListener(BusinessEvictCacheEvent.class)
    public void evictCache(){
        this.businessService.deleteBusinessesCache();
    }
}
