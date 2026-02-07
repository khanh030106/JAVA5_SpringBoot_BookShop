package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Services.CartItemsService;
import assignment.java5.ass_self.Services.CustomerUserDetail;
import assignment.java5.ass_self.Services.UserService;
import assignment.java5.ass_self.util.IsLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {
    private final CartItemsService cartItemsService;
    private final UserService userService;
    public CartController(CartItemsService cartItemsService,
                          UserService userService
    ){
        this.cartItemsService = cartItemsService;
        this.userService = userService;
    }

    @GetMapping("/bookseller/cart")
    public String cartItems(Model model,
                            Authentication authentication
                            ){
        if (authentication == null || !IsLogin.isAuthenticated()){
            return "redirect:/bookseller/login";

        }

        Object principal = authentication.getPrincipal();

        User currentUser = null;
        if (principal instanceof CustomerUserDetail){
            currentUser = ((CustomerUserDetail) principal).getUser();
        }else if (principal instanceof OAuth2User){
            String email = (String) ((OAuth2User) principal).getAttributes().get("email");
            currentUser = userService.findUserByEmail(email).orElse(null);
        }
        if (currentUser == null){
            return "redirect:/bookseller/login";
        }
        Long userID = currentUser.getId();
        model.addAttribute("cartItems", cartItemsService.findAllCartItemsByUser(userID));
        return "Fragments/cart_fragment";
    }
}
