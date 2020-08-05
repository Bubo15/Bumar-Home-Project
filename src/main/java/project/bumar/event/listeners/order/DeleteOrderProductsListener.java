package project.bumar.event.listeners.order;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.bumar.data.entities.Order;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.event.events.email.SendProductDeletedEmailEvent;
import project.bumar.event.events.order.DeleteOrderProductsEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.service.models.order.OrderServiceModel;
import project.bumar.service.services.OrderService;

@Component
public class DeleteOrderProductsListener {

    private final Publisher publisher;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public DeleteOrderProductsListener(Publisher publisher, OrderService orderService, ModelMapper modelMapper) {
        this.publisher = publisher;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @EventListener(DeleteOrderProductsEvent.class)
    public void deleteFromOrdersGivenProduct(DeleteOrderProductsEvent deleteOrderProductsEvent) {
        for (OrderServiceModel order : this.orderService.getAllOrders()) {
            for (BaseProduct givenProduct : deleteOrderProductsEvent.getProducts()) {
                if (order.getProducts().containsKey(givenProduct)) {
                    order.getProducts().remove(givenProduct);
                    this.publisher.publishEvent(new SendProductDeletedEmailEvent(this, givenProduct, order.getEmail()));
                    this.deductPrice(this.modelMapper.map(order, Order.class), givenProduct);
                }
            }
        }
    }

    private void deductPrice(Order order, BaseProduct product) {
        order.setTotalPrice(order.getTotalPrice() - product.getPrice());
        if (order.getTotalPrice() == 0) {
            order.setTotalPrice(0);
        }
        this.orderService.save(order);
    }

}
