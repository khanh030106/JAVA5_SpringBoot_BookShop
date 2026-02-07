package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Entities.Cart;
import assignment.java5.ass_self.Entities.CartItem;
import assignment.java5.ass_self.Entities.CartItemId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItem, CartItemId> {

    @Query("""
    SELECT ci FROM CartItem ci 
    WHERE ci.cartID.userID.id = :userID
""")
    List<CartItem> findAllCartItemsByUser(@Param("userID")Long id, Pageable pageable);
}
