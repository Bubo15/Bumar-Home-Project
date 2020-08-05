package project.bumar.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bumar.data.entities.Service;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> getServiceById(long id);

    Optional<Service> getServiceByName(String name);
}
