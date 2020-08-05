package project.bumar.event.events.file;

import project.bumar.event.events.Event;

public class DeleteImageEvent extends Event {

    private final long id;

    public DeleteImageEvent(Object source, long id) {
        super(source);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
