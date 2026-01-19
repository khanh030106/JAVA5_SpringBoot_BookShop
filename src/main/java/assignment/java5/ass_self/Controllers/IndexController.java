package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.Services.BooksService;
import assignment.java5.ass_self.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final BooksService booksService;
    private final CategoryService categoryService;
    public IndexController(BooksService booksService,
                           CategoryService categoryService
    ){
        this.categoryService = categoryService;
        this.booksService = booksService;
    }

    @GetMapping("/bookseller/home")
    public String home(Model model){

            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("topSoldOutBooks", booksService.top20SoldOut());
            model.addAttribute("find30Books", booksService.find30Books());
            model.addAttribute("title_top_sold_out", "Sách bán chạy nhất");
            model.addAttribute("subtitle_top_sold_out", "Những tác phẩm bán chạy nhất trong tháng");
            model.addAttribute("title_all_books", "Các thể loại sách khác");
            model.addAttribute("subtitle_all_books", "Khám phá các thể loại sách đa dạng");
        return "indexPage";
    }
}
