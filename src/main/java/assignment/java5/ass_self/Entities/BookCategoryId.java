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
public class BookCategoryId implements Serializable {
    private static final long serialVersionUID = -4081944054778558397L;
    @NotNull
    @Column(name = "BookID", nullable = false)
    private Long bookID;

    @NotNull
    @Column(name = "CategoryID", nullable = false)
    private Integer categoryID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookCategoryId entity = (BookCategoryId) o;
        return Objects.equals(this.categoryID, entity.categoryID) &&
                Objects.equals(this.bookID, entity.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryID, bookID);
    }

}