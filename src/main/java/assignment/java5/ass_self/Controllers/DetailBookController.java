package assignment.java5.ass_self.Controllers;
import assignment.java5.ass_self.Services.BooksService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailBookController {
    private final BooksService booksService;
    public DetailBookController(BooksService booksService
    ){
        this.booksService = booksService;
    }


    @GetMapping("/bookseller/detail/{id}")
    public String detailProduct(@PathVariable Long id, Model model){
        model.addAttribute("same_cate_books", booksService.findBookWithCate(id));
        model.addAttribute("detail_book", booksService.findBookById(id));
        return "Fragments/detail_fragment";
    }

}
