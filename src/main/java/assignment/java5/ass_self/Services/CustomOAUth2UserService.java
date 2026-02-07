package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAUth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public CustomOAUth2UserService(UserService userService){
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        User user = userService.findUserByEmail(email).orElse(null);
        return oAuth2User;
    }
}
