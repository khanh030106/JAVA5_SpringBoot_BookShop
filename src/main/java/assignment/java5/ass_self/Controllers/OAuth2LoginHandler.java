package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.Services.AuthService;
import assignment.java5.ass_self.enums.AuthProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, SecurityException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        System.out.println("OAuth2 User Email: " + email);

        String registrationId = ((OAuth2AuthenticationToken)authentication).getAuthorizedClientRegistrationId();

        String avatar;
        AuthProvider provider;

        if (registrationId.equals("google")) {
            avatar = oAuth2User.getAttribute("picture");
            provider = AuthProvider.GOOGLE;
        } else {
            avatar = null;
            provider = AuthProvider.LOCAL;
        }

        authService.processOAuthPostLogin(email, name, avatar, provider);
        response.sendRedirect("/bookseller/home");
    }
}
