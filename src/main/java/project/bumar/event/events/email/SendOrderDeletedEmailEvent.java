package project.bumar.event.events.email;

import project.bumar.data.entities.Order;
import project.bumar.event.events.Event;

public class SendOrderDeletedEmailEvent extends Event {

    private final Order order;

    public SendOrderDeletedEmailEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
