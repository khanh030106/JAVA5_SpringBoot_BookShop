package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.Wishlist;
import assignment.java5.ass_self.Entities.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<Wishlist, WishlistId> {

}
