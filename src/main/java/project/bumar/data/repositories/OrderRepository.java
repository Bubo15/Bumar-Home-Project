package project.bumar.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bumar.data.entities.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> getOrderById(long id);
}
