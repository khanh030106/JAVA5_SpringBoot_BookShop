package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    public void sendVerifyMail(User user, String token) {

        String link = "http://localhost:8080/bookseller/verify?token=" + token;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Xác nhận đăng ký tài khoản");
        mail.setText("""
                    Chào %s,

                    Vui lòng nhấp vào liên kết sau để xác nhận tài khoản:
                    %s

                    Liên kết có hiệu lực trong 2 phút.

                    Nếu bạn không đăng ký, hãy bỏ qua email này.
                    """.formatted(user.getFullName(), link));
        javaMailSender.send(mail);

    }
}
