package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
