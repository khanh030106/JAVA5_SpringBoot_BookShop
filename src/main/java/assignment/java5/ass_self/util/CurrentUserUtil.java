package assignment.java5.ass_self.util;

import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Services.CustomerUserDetail;
import assignment.java5.ass_self.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {

    private final UserService userService;

    public CurrentUserUtil(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // Case anonymous
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return null;
        }

        // Login thường
        if (principal instanceof CustomerUserDetail) {
            return ((CustomerUserDetail) principal).getUser();
        }

        // Login Google OAuth2
        if (principal instanceof OAuth2User) {
            String email = ((OAuth2User) principal).getAttribute("email");

            if (email == null) return null;

            return userService.findUserByEmail(email).orElse(null);
        }

        return null;
    }
}

