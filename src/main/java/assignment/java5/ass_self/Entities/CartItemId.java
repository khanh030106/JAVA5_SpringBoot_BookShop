package assignment.java5.ass_self.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CartItemId implements Serializable {
    private static final long serialVersionUID = 3186440934983881703L;
    @NotNull
    @Column(name = "CartID", nullable = false)
    private Long cartID;

    @NotNull
    @Column(name = "BookID", nullable = false)
    private Long bookID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CartItemId entity = (CartItemId) o;
        return Objects.equals(this.cartID, entity.cartID) &&
                Objects.equals(this.bookID, entity.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartID, bookID);
    }

}