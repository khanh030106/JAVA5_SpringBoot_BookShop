package assignment.java5.ass_self.Controllers;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Services.AuthorsService;
import assignment.java5.ass_self.Services.BooksService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private final BooksService booksService;
    private final AuthorsService authorsService;
    public HomeController(BooksService booksService,
                          AuthorsService authorsService
                          ){
        this.booksService = booksService;
        this.authorsService = authorsService;
    }

    @GetMapping("bookseller/home/moreboook")
    public String bookFragment(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int index,
            @RequestParam(defaultValue = "40") int pageSize,
            Model model
    ) {

        Page<Book> page;

        if ("topSoldOut".equals(type)) {

            page = booksService.topSoldOut(index, pageSize);

            model.addAttribute("title", "Sách bán chạy nhất");
            model.addAttribute("subtitle", "Những tác phẩm bán chạy nhất trong tháng");
            model.addAttribute("showHot", true);

        }
        else if ("allBooks".equals(type)) {

            page = booksService.findAllBooks(index, pageSize);

            model.addAttribute("title", "Các thể loại sách khác");
            model.addAttribute("subtitle", "Khám phá các thể loại sách đa dạng");
            model.addAttribute("showHot", false);

        } else if ("promtionBooks".equals(type)) {

            page = booksService.findAllPromotionBooks(index, pageSize);

            model.addAttribute("title", "Sách khuyến mãi");
            model.addAttribute("subtitle", "Những cuốn sách đang được giảm giá hấp dẫn");
            model.addAttribute("showHot", false);

        } else {

            page = booksService.findAllBooks(index, pageSize);

            model.addAttribute("title", "Tất cả sách");
            model.addAttribute("subtitle", "Danh sách đầy đủ các sách hiện có");
            model.addAttribute("showHot", false);
        }

        model.addAttribute("type", type);
        model.addAttribute("books", page.getContent());
        model.addAttribute("authors", authorsService.getAllAuthors());
        model.addAttribute("currentPage", index);
        model.addAttribute("totalPages", page.getTotalPages());

        return "Fragments/home_fragments/home_fragment :: content";
    }


    @GetMapping("/bookseller/home/morebook/filterbooks" )
    public String filterBooks(@RequestParam(required = false) String price,
                              @RequestParam(required = false) Long category,
                              @RequestParam(required = false) String author,
                              Model model
    ){
        List<Book> books = booksService.filterBooks(price, category, author);
        model.addAttribute("books", books);
        model.addAttribute("title", "Kết quả lọc sách");
        model.addAttribute("subtitle", "Các sách được lọc theo tiêu chí của bạn");
        model.addAttribute("authors", authorsService.getAllAuthors());
        model.addAttribute("showHot", false);
        return "Fragments/home_fragments/home_fragment :: content";
    }
}
