package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Entities.CartItem;
import assignment.java5.ass_self.Repository.CartItemsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemsService {

    private final CartItemsRepository cartItemsRepository;
    public CartItemsService(CartItemsRepository cartItemsRepository){
        this.cartItemsRepository = cartItemsRepository;
    }

    public List<CartItem> findAllCartItemsByUser(Long id){
        Pageable pageable = PageRequest.of(0, 10);
        return cartItemsRepository.findAllCartItemsByUser(id, pageable);
    }
}
