package assignment.java5.ass_self.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendVerifyMail(String to, String link){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Link xác thực tài khoản");
        mailMessage.setText("Nhấn vào link này để xác thực tài khoản: \n" +link);

        javaMailSender.send(mailMessage);
    }
}
