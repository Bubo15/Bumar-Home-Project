package project.bumar.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bumar.data.entities.Business;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {


    Optional<Business> getBusinessById(long id);

    Optional<Business> getBusinessByName(String name);

    void deleteBusinessById(long id);
}
