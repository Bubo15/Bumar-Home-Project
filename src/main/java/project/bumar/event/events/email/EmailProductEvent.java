package project.bumar.event.events.email;

import project.bumar.data.entities.base.BaseProduct;
import project.bumar.event.events.Event;

public abstract class EmailProductEvent extends Event {

    private final BaseProduct product;

    public EmailProductEvent(Object source, BaseProduct product) {
        super(source);
        this.product = product;
    }

    public BaseProduct getProduct() {
        return product;
    }
}
