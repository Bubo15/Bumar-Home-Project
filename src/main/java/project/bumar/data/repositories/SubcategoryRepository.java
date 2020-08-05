package project.bumar.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.bumar.data.entities.SubCategory;

import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubCategory, Long> {

    Optional<SubCategory> getSubCategoryByNameAndCategoryName(String subcategoryName, String categoryName);

    Optional<SubCategory> getSubCategoryByName(String name);

    void deleteByName(String name);
}
