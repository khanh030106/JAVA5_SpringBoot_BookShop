package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Services.AuthorsService;
import assignment.java5.ass_self.Services.BooksService;
import assignment.java5.ass_self.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private final BooksService booksService;
    private final AuthorsService authorsService;
    private final CategoryService categoryService;
    public HomeController(BooksService booksService,
                          AuthorsService authorsService,
                          CategoryService categoryService
                          ){
        this.booksService = booksService;
        this.authorsService = authorsService;
        this.categoryService = categoryService;
    }

    @GetMapping("bookseller/home/moreboook")
    public String bookFragment(@RequestParam(required = false) String type,
                               Model model
    ){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("authors", authorsService.getAllAuthors());
        if("topSoldOut".equals(type)){
            model.addAttribute("books", booksService.topSoldOut());
            model.addAttribute("title", "Sách bán chạy nhất");
            model.addAttribute("subtitle", "Những tác phẩm bán chạy nhất trong tháng");
            model.addAttribute("showHot", true);
        } else if ("allBooks".equals(type)) {
            model.addAttribute("books", booksService.findAllBooks());
            model.addAttribute("title", "Các thể loại sách khác");
            model.addAttribute("subtitle", "Khám phá các thể loại sách đa dạng");
            model.addAttribute("showHot", false);
        }
        return "Fragments/home_fragments/home_fragment :: content";
    }

    @GetMapping("/bookseller/home/morebook/filterbooks" )
    public String filterBooks(@RequestParam(required = false) String price,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) String author,
                              Model model
    ){
        List<Book> books = booksService.filterBooks(price, category, author);
        model.addAttribute("books", books);
        model.addAttribute("title", "Kết quả lọc sách");
        model.addAttribute("subtitle", "Các sách được lọc theo tiêu chí của bạn");
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("authors", authorsService.getAllAuthors());
        model.addAttribute("showHot", false);
        return "Fragments/home_fragments/home_fragment :: content";
    }
}
