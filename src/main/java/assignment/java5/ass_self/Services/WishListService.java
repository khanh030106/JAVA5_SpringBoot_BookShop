package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Entities.Wishlist;
import assignment.java5.ass_self.Repository.WishListRepository;

public class WishListService {
    private final WishListRepository wishListRepository;
    private final AuthService authService;
    private final BooksService booksService;

    public WishListService(WishListRepository wishListRepository,
                           AuthService authService,
                           BooksService booksService
                           ){
        this.wishListRepository = wishListRepository;
        this.authService = authService;
        this.booksService = booksService;
    }

    public void addToWishList(Long userID, Long bookID){
        User user = authService.findById(userID);
        Book book = booksService.findBookWithId(bookID);

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setBook(book);

        wishListRepository.save(wishlist);
    }
}
