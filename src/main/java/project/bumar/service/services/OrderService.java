package project.bumar.service.services;

import project.bumar.data.entities.Order;
import project.bumar.service.models.order.OrderServiceModel;
import project.bumar.service.models.order.OrderUserDetailsServiceModel;
import project.bumar.web.models.bindingModels.order.OrderCreateBindingModel;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<OrderServiceModel> create(OrderCreateBindingModel orderCreateBindingModel);

    List<OrderServiceModel> getAllOrders();

    List<OrderUserDetailsServiceModel> getAllOrdersByCurrentUser(String username);

    void save(Order order);

    OrderUserDetailsServiceModel deleteById(long id);

    Order getOrderById(long id);
}
