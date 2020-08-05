package project.bumar.aop.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.bumar.constant.ErrorConstants;
import project.bumar.exeption.TimeIsUpException;
import project.bumar.service.models.order.OrderServiceModel;
import project.bumar.service.services.OrderService;

import java.time.LocalDateTime;

@Aspect
@Component
public class DeleteOrderAspect {

    private final OrderService orderService;

    @Autowired
    public DeleteOrderAspect(OrderService orderService) {
        this.orderService = orderService;
    }

    @After("execution(* project.bumar.event.listeners.order.DeleteOrderProductsListener.deleteFromOrdersGivenProduct(..))")
    private void deleteOrderIfHasNotProduct(){
        for (OrderServiceModel order : this.orderService.getAllOrders()) {
            if (order.getProducts().size() == 0){
                this.orderService.deleteById(order.getId());
            }
        }
    }

    @Before("execution(* project.bumar.web.controllers.OrderController.deleteById(..))")
    private void deleteOrderIfNotPassingTwoDaysFromCreation(JoinPoint joinPoint){
         long id = Long.parseLong(joinPoint.getArgs()[0].toString());

        LocalDateTime orderDeleteDeadLine = this.orderService.getOrderById(id).getOrdered().plusDays(2);

         if (orderDeleteDeadLine.isBefore(LocalDateTime.now())){
             throw new TimeIsUpException(ErrorConstants.DELETE_ORDER_TIME_IS_UP);
         }
    }
}
