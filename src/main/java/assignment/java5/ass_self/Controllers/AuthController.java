package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.DTO.LoginRequest;
import assignment.java5.ass_self.DTO.RegisterRequest;
import assignment.java5.ass_self.Services.AuthService;
import assignment.java5.ass_self.Services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(
                          AuthService authService
    ) {
        this.authService = authService;
    }

    @GetMapping("/bookseller/register")
    public String registerForm(
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String verify,
            Model model
    ) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        model.addAttribute("verify", verify);
        model.addAttribute("request", new RegisterRequest());
        return "Fragments/register_fragment";
    }

    @PostMapping("/bookseller/register")
    public String register(
            @Valid @ModelAttribute("request") RegisterRequest request,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "Fragments/register_fragment";
        }

        if (!request.getPassword().equals(request.getConfirm_password())) {
            model.addAttribute("confirmPasswordError", "Mật khẩu không khớp");
            return "Fragments/register_fragment";
        }

        String errors = authService.register(request);

        if (errors != null) {
            model.addAttribute("existEmail", errors);
            return "Fragments/register_fragment";
        }

        return "redirect:/bookseller/register?success=true";
    }


    @GetMapping("/bookseller/verify")
    public String verifyAccount(
            @RequestParam String token,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authService.verifyAccount(token);
            redirectAttributes.addAttribute("verify", "success");
        } catch (Exception e) {
            redirectAttributes.addAttribute("verify", "fail");
        }
        return "redirect:/bookseller/register";
    }

    @GetMapping("/bookseller/login")
    public String loginForm(Model model) {
        model.addAttribute("request", new LoginRequest());
        return "Fragments/login_fragment";
    }
}
