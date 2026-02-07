package assignment.java5.ass_self.Controllers;
import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Repository.UsersRepository;
import assignment.java5.ass_self.Services.BooksService;
import assignment.java5.ass_self.Services.CustomerUserDetail;
import assignment.java5.ass_self.Services.UserService;
import assignment.java5.ass_self.util.IsLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;
import java.util.Optional;

@Controller
public class IndexController {
    private final BooksService booksService;
    public IndexController(
            BooksService booksService
    ){
        this.booksService = booksService;
    }

    @GetMapping("/bookseller/home")
    public String home(Model model){

            model.addAttribute("topSoldOutBooks", booksService.top20SoldOut());
            model.addAttribute("find20Books", booksService.find20Books());
            model.addAttribute("promotionBooks", booksService.find25PromotionBooks());
            model.addAttribute("title_top_sold_out", "Sách bán chạy nhất");
            model.addAttribute("subtitle_top_sold_out", "Những tác phẩm bán chạy nhất trong tháng");
            model.addAttribute("title_all_books", "Các thể loại sách khác");
            model.addAttribute("subtitle_all_books", "Khám phá các thể loại sách đa dạng");
            model.addAttribute("title_promotion_books", "Sách khuyến mãi");
            model.addAttribute("subtitle_promotion_books", "Những cuốn sách đang được giảm giá hấp dẫn");

        return "indexPage";
    }
}
