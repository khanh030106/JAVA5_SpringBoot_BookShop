package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorsRepository extends JpaRepository<Author, Long> {
}
