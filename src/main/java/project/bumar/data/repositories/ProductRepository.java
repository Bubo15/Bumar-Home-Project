package project.bumar.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bumar.data.entities.base.BaseProduct;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<BaseProduct, Long> {

    Optional<BaseProduct> getBaseProductById(long id);
}
