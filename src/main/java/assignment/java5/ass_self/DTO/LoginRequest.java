package assignment.java5.ass_self.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotNull(message = "Mật khẩu không được để trống")
    private String email;
    @NotNull(message = "Mật khẩu không được để trống")
    private String password;
}
