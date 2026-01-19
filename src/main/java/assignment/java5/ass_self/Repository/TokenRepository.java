package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    @Query("""
    SELECT e FROM EmailVerificationToken e WHERE e.token = :token
""")
    EmailVerificationToken findValidToken(String token);
}
