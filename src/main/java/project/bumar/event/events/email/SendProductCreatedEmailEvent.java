package project.bumar.event.events.email;

import project.bumar.data.entities.base.BaseProduct;

public class SendProductCreatedEmailEvent extends EmailProductEvent {

    public SendProductCreatedEmailEvent(Object source, BaseProduct product) {
        super(source, product);
    }
}
