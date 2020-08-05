package project.bumar.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bumar.data.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
