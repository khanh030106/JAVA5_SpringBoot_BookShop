package assignment.java5.ass_self.Controllers;
import assignment.java5.ass_self.Services.BooksService;
import assignment.java5.ass_self.Services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailBookController {
    private final BooksService booksService;
    private final CategoryService categoryService;
    public DetailBookController(BooksService booksService,
                                CategoryService categoryService
    ){
        this.booksService = booksService;
        this.categoryService = categoryService;
    }


    @GetMapping("/bookseller/detail/{id}")
    public String detailProduct(@PathVariable Long id, Model model){
        model.addAttribute("same_cate_books", booksService.findBookWithCate(id));
        model.addAttribute("detail_book", booksService.findBookById(id));
        model.addAttribute("categories", categoryService.getAllCategory());
        return "Fragments/detail_fragment";
    }

}
