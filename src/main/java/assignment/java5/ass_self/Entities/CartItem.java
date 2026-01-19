package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CartItems")
public class CartItem {
    @EmbeddedId
    private CartItemId id;

    @MapsId("cartID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CartID", nullable = false)
    private Cart cartID;

    @MapsId("bookID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookID", nullable = false)
    private Book bookID;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

}