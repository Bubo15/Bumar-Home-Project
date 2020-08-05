package project.bumar.event.events.order;

import project.bumar.data.entities.base.BaseProduct;
import project.bumar.event.events.Event;

import java.util.Set;

public class DeleteOrderProductsEvent extends Event {

    private final Set<BaseProduct> products;

    public DeleteOrderProductsEvent(Object source, Set<BaseProduct> products) {
        super(source);
        this.products = products;
    }

    public Set<BaseProduct> getProducts() {
        return products;
    }
}
