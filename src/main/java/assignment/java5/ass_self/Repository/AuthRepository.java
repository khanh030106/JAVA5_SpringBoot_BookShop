package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.EmailVerificationToken;
import assignment.java5.ass_self.Entities.Role;
import assignment.java5.ass_self.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

    @Query("""
    SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email\s
""")
    boolean existByEmail(@Param("email") String email);

    @Query("""
    SELECT r FROM Role r  WHERE r.roleName = :roleName
""")
    Optional<Role> findByRoleName(@Param("roleName") String roleName);

    @Modifying
    @Query(value = """
    INSERT INTO UserRoles(userID, roleID) VALUES (:userID, :roleID)
""", nativeQuery = true)
    void inserUserRole(@Param("userID") Long userID, @Param("roleID") Integer roleID);

}