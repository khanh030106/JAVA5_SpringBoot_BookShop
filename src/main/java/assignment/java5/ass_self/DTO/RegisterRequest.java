package assignment.java5.ass_self.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    private String fullname;
    private String email;
    private String gender;
    private LocalDate dob;
    private String password;
    private String confirm_password;
}
