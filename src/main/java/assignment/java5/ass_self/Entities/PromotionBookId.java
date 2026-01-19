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
public class PromotionBookId implements Serializable {
    private static final long serialVersionUID = -8065672424537348910L;
    @NotNull
    @Column(name = "PromotionID", nullable = false)
    private Long promotionID;

    @NotNull
    @Column(name = "BookID", nullable = false)
    private Long bookID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PromotionBookId entity = (PromotionBookId) o;
        return Objects.equals(this.promotionID, entity.promotionID) &&
                Objects.equals(this.bookID, entity.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promotionID, bookID);
    }

}