package project.bumar.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Order;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.data.repositories.OrderRepository;
import project.bumar.event.events.email.SendOrderDeletedEmailEvent;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.EmptyKeyOrValueException;
import project.bumar.exeption.NotFoundException;
import project.bumar.service.models.order.OrderServiceModel;
import project.bumar.service.models.order.OrderUserDetailsServiceModel;
import project.bumar.service.services.OrderService;
import project.bumar.service.services.ProductService;
import project.bumar.validate.Validator;
import project.bumar.web.models.bindingModels.order.OrderCreateBindingModel;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final Publisher publisher;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService, ModelMapper modelMapper, Validator validator, Publisher publisher) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.publisher = publisher;
    }

    @Override
    public Optional<OrderServiceModel> create(OrderCreateBindingModel orderCreateBindingModel) {

        if (this.validator.isThereOrderProductError(orderCreateBindingModel.getProducts())){
            throw new EmptyKeyOrValueException(ErrorConstants.ORDER_PRODUCT_EMPTY_KEY_OR_VALUE);
        }

        Order order = this.modelMapper.map(orderCreateBindingModel, Order.class);
        order.setProducts(setProducts(orderCreateBindingModel.getProducts()));
        order.setOrdered(LocalDateTime.now());

        this.orderRepository.saveAndFlush(order);

        return Optional.of(this.modelMapper.map(order, OrderServiceModel.class));
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        return this.orderRepository
                .findAll()
                .stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderUserDetailsServiceModel> getAllOrdersByCurrentUser(String username) {
        return this.orderRepository
                .findAll()
                .stream()
                .filter(order -> order.getUsername().equalsIgnoreCase(username))
                .map(order -> {
                    OrderUserDetailsServiceModel oudsm = this.modelMapper.map(order, OrderUserDetailsServiceModel.class);
                    oudsm.setCountOfProducts(order.getProducts().size());
                    return oudsm;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void save(Order order) {
        this.orderRepository.saveAndFlush(order);
    }

    @Override
    @Transactional
    public OrderUserDetailsServiceModel deleteById(long id) {

        Order order = this.getNativeOrderById(id);

        this.publisher.publishEvent(new SendOrderDeletedEmailEvent(this, this.getNativeOrderById(id)));

        OrderUserDetailsServiceModel orderUserDetailsServiceModel = this.modelMapper.map(order, OrderUserDetailsServiceModel.class);

        orderUserDetailsServiceModel.setCountOfProducts(order.getProducts().size());

        this.orderRepository.deleteById(id);

        return orderUserDetailsServiceModel;
    }

    private Map<BaseProduct, Integer> setProducts(Set<Map<String, Integer>> productsBinding){
        Map<BaseProduct, Integer> products = new HashMap<>();

        for (Map<String, Integer> map : productsBinding) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {

                BaseProduct baseProduct = this.productService.getProductById(Long.parseLong(entry.getKey()));

                setProductNewCount(baseProduct, entry.getValue());

                products.put(baseProduct, entry.getValue());
            }
        }

        return products;
    }

    @Override
    public Order getOrderById(long id){
        return this.getNativeOrderById(id);
    }

    private void setProductNewCount(BaseProduct product, int count){
        product.setCountOfProduct(product.getCountOfProduct() - count);
        this.productService.saveProduct(product);
    }

    private Order getNativeOrderById(long id){
        return this.orderRepository.getOrderById(id).orElseThrow(() -> new NotFoundException(ErrorConstants.ORDER_ID_NOT_FOUND));
    }
}
