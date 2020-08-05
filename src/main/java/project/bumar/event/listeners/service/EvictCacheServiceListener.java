package project.bumar.event.listeners.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.bumar.event.events.service.ServiceEvictCacheEvent;
import project.bumar.service.services.ServiceService;

@Component
public class EvictCacheServiceListener {

    private final ServiceService serviceService;

    @Autowired
    public EvictCacheServiceListener(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @EventListener(ServiceEvictCacheEvent.class)
    public void evictCache(){
        this.serviceService.deleteServicesCache();
    }
}
