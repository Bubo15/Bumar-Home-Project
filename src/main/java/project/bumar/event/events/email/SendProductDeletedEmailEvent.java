package project.bumar.event.events.email;

import project.bumar.data.entities.base.BaseProduct;

public class SendProductDeletedEmailEvent extends EmailProductEvent {

    private String userEmail;

    public SendProductDeletedEmailEvent(Object source, BaseProduct product, String userEmail) {
        super(source, product);
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
