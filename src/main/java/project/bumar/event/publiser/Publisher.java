package project.bumar.event.publiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import project.bumar.event.events.Event;

@Component
public class Publisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    protected Publisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEvent(Event event){
        this.applicationEventPublisher.publishEvent(event);
    }
}
