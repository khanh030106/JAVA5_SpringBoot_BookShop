package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String name);
}
