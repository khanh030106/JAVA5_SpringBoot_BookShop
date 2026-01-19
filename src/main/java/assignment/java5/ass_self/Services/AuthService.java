package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.DTO.RegisterRequest;
import assignment.java5.ass_self.Entities.EmailVerificationToken;
import assignment.java5.ass_self.Entities.Role;
import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Repository.AuthRepository;
import assignment.java5.ass_self.Repository.TokenRepository;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JavaMailSender javaMailSender;

    public AuthService(AuthRepository authRepository,
                       PasswordEncoder passwordEncoder,
                       TokenRepository tokenRepository,
                       JavaMailSender javaMailSender
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.javaMailSender = javaMailSender;
    }

        public void register(RegisterRequest request) {

            if (authRepository.existByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại");
            }

            if (!request.getPassword().equals(request.getConfirm_password())) {
                throw new RuntimeException("Mật khẩu xác nhận không khớp");
            }

            User user = new User();
            user.setFullName(request.getFullname());
            user.setEmail(request.getEmail());
            user.setGender(request.getGender());
            user.setDateOfBirth(request.getDob());
            user.setIsActive(false);
            user.setIsDeleted(false);
            user.setPasswordHash(
                    passwordEncoder.encode(request.getPassword())
            );

            authRepository.save(user);

            Role adminRole = authRepository
                    .findByRoleName("Admin")
                    .orElseThrow(() -> new RuntimeException("Role Admin không tồn tại"));

            authRepository.inserUserRole(user.getId(), adminRole.getId());

            EmailVerificationToken token = new EmailVerificationToken();
            token.setUserID(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiredAt(
                    Instant.now().plus(15, ChronoUnit.MINUTES)
            );
            token.setIsUsed(false);

            tokenRepository.save(token);

            sendVerifyMail(user, token.getToken());
        }

        public void verifyAccount(String tokenStr) {

            EmailVerificationToken token =
                    tokenRepository.findValidToken(tokenStr);

            if (token == null) {
                throw new RuntimeException("Token không tồn tại");
            }

            if (token.getIsUsed()) {
                throw new RuntimeException("Token đã được sử dụng");
            }

            if (token.getExpiredAt().isBefore(Instant.now())) {
                throw new RuntimeException("Token đã hết hạn");
            }

            User user = token.getUserID();
            user.setIsActive(true);

            token.setIsUsed(true);

            authRepository.save(user);
            tokenRepository.save(token);
        }

        private void sendVerifyMail(User user, String token) {

            String link =
                    "http://localhost:8080/bookseller/verify?token=" + token;
            System.out.println("http://localhost:8080/bookseller/verify?token=" + token);

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(user.getEmail());
            mail.setSubject("Xác nhận đăng ký tài khoản");
            mail.setText("""
                    Chào %s,

                    Vui lòng nhấp vào liên kết sau để xác nhận tài khoản:
                    %s

                    Liên kết có hiệu lực trong 15 phút.

                    Nếu bạn không đăng ký, hãy bỏ qua email này.
                    """.formatted(user.getFullName(), link));
            javaMailSender.send(mail);

        }
    }
