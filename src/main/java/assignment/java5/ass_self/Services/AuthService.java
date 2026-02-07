package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.DTO.LoginRequest;
import assignment.java5.ass_self.DTO.RegisterRequest;
import assignment.java5.ass_self.Entities.EmailVerificationToken;
import assignment.java5.ass_self.Entities.Role;
import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Repository.AuthRepository;
import assignment.java5.ass_self.Repository.TokenRepository;
import assignment.java5.ass_self.enums.AuthProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final MailService mailService;

    public AuthService(AuthRepository authRepository,
                       PasswordEncoder passwordEncoder,
                       TokenRepository tokenRepository,
                       MailService mailService
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
    }

        public String register(RegisterRequest request) {

            if (authRepository.existByEmail(request.getEmail())) {
                return "Email đã tồn tại";
            }

            User user = new User();
            user.setFullName(request.getFullname());
            user.setEmail(request.getEmail());
            user.setGender(request.getGender());
            user.setDateOfBirth(request.getDob());
            user.setIsActive(false);
            user.setIsDeleted(false);
            user.setProvider(AuthProvider.LOCAL);
            user.setPasswordHash(
                    passwordEncoder.encode(request.getPassword())
            );

            authRepository.save(user);

            Role adminRole = authRepository
                    .findByRoleName("ROLE_CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("Role Customer không tồn tại"));

            authRepository.inserUserRole(user.getId(), adminRole.getId());

            EmailVerificationToken token = new EmailVerificationToken();
            token.setUserID(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiredAt(
                    Instant.now().plus(2, ChronoUnit.MINUTES)
            );
            token.setIsUsed(false);

            tokenRepository.save(token);

            mailService.sendVerifyMail(user, token.getToken());

            return null;
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

        public User processOAuthPostLogin(String email,
                                          String fullName,
                                          String avatarUrl,
                                          AuthProvider provider

        ) {
            Optional<User> existUser = authRepository.findByEmail(email);

            if (existUser.isPresent()) {
                User u = existUser.get();

                if (u.getProvider() != provider) {
                    throw new RuntimeException("Bạn đã đăng ký với " + u.getProvider() +
                            ". Vui lòng sử dụng " + u.getProvider() + " để đăng nhập.");
                }
                return u;
            } else {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setFullName(fullName);
                newUser.setAvatar(avatarUrl);
                newUser.setIsActive(true);
                newUser.setIsDeleted(false);
                newUser.setProvider(provider);
                newUser.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
                authRepository.save(newUser);

                Role userRole = authRepository
                        .findByRoleName("ROLE_CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("Role Customer không tồn tại"));

                authRepository.inserUserRole(newUser.getId(), userRole.getId());

                return newUser;
            }
        }

        public User findById(Long id){
            return authRepository.findById(id).orElse(null);
        }
    }
