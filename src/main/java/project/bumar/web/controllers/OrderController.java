package project.bumar.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bumar.service.services.OrderService;
import project.bumar.web.models.bindingModels.order.OrderCreateBindingModel;
import project.bumar.web.models.responseModel.order.OrderResponseModel;
import project.bumar.web.models.responseModel.order.OrderUserDetailsResponseModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = {"http://localhost:3000"})
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseModel> create(@RequestPart OrderCreateBindingModel orderCreateBindingModel,
                                                     BindingResult result) throws URISyntaxException {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(new URI("/order/create"))
                .body(this.modelMapper.map(this.orderService.create(orderCreateBindingModel).orElseThrow(), OrderResponseModel.class));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderUserDetailsResponseModel>> getAllByCurrentUser(Principal principal){
        return ResponseEntity.ok(
                this.orderService
                .getAllOrdersByCurrentUser(principal.getName())
                .stream()
                .map(orderServiceModel -> this.modelMapper.map(orderServiceModel, OrderUserDetailsResponseModel.class))
                .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<OrderUserDetailsResponseModel> deleteById(@PathVariable long id){
        return ResponseEntity.ok(this.modelMapper.map(this.orderService.deleteById(id), OrderUserDetailsResponseModel.class));
    }
}
