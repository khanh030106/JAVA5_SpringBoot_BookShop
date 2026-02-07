package assignment.java5.ass_self.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class layoutController {

    @GetMapping("/bookseller/orders")
    public String orders(){
        return "Fragments/orders_fragment";
    }

    @GetMapping("/bookseller/ordersDetails")
    public String orderDetails(){
        return "Fragments/orderDetails_fragment";
    }

    @GetMapping("/bookseller/wishlist")
    public String wishList(){
        return "Fragments/wishlist_fragment";
    }

    @GetMapping("/bookseller/admin/categories")
    public String categoriesManagement(){
        return "Fragments/manager_fragments/categories_management";
    }

    @GetMapping("/bookseller/admin/books")
    public String booksManagement(){
        return "Fragments/manager_fragments/books_management";
    }

    @GetMapping("/bookseller/admin/users")
    public String usersManagement(){
        return "Fragments/manager_fragments/users_management";
    }

    @GetMapping("/bookseller/admin/orders")
    public String ordersManagement(){
        return "Fragments/manager_fragments/orders_management";
    }

    @GetMapping("/bookseller/admin/revenue")
    public String revenueAnalytics(){
        return "Fragments/manager_fragments/revenue_analytics";
    }

    @GetMapping("/bookseller/admin/topCustomers")
    public String topCustomer(){
        return "Fragments/manager_fragments/top_customer";
    }

    @GetMapping("/bookseller/profile")
    public String profile(){
        return "Fragments/profile_fragment";
    }
}
