package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.Entities.Category;
import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Services.CategoryService;
import assignment.java5.ass_self.Services.CustomerUserDetail;
import assignment.java5.ass_self.Services.UserService;
import assignment.java5.ass_self.util.IsLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttribute {
    private final CategoryService categoryService;
    private final UserService userService;

    public GlobalModelAttribute(
            CategoryService categoryService,
            UserService userService
    ){
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @ModelAttribute("categories")
    public List<Category> categories(){
        return categoryService.getAllCategory();
    }

    @ModelAttribute("currentUser")
    public User user(Authentication authentication){
        if (authentication == null || !IsLogin.isAuthenticated()){
            return null;
        }

        Object principal = authentication.getPrincipal();

        User currentUser = null;
            if (principal instanceof CustomerUserDetail){
                currentUser = ((CustomerUserDetail) principal).getUser();
            }else if (principal instanceof OAuth2User){
                String email = (String) ((OAuth2User) principal).getAttributes().get("email");
                currentUser = userService.findUserByEmail(email).orElse(null);
        }
        return currentUser;
    }
}
