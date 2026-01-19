package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.DTO.RegisterRequest;
import assignment.java5.ass_self.Services.AuthService;
import assignment.java5.ass_self.Services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final CategoryService categoryService;
    private final AuthService authService;
    public AuthController(CategoryService categoryService,
                          AuthService authService
                          ){
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @GetMapping("/bookseller/register")
    public String registerForm(
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String error,
            Model model
    ) {
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "Fragments/register_fragment";
    }


    @GetMapping("/bookseller/login")
    public String loginForm(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "Fragments/login_fragment";
    }

    @PostMapping("/bookseller/register")
    public String register(
            @ModelAttribute RegisterRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            authService.register(request);
            redirectAttributes.addAttribute("success", "true");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/bookseller/register";
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
        return "redirect:/bookseller/login";
    }


}
