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
public class BookAuthorId implements Serializable {
    private static final long serialVersionUID = -3006634205004129993L;
    @NotNull
    @Column(name = "BookID", nullable = false)
    private Long bookID;

    @NotNull
    @Column(name = "AuthorID", nullable = false)
    private Long authorID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookAuthorId entity = (BookAuthorId) o;
        return Objects.equals(this.authorID, entity.authorID) &&
                Objects.equals(this.bookID, entity.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorID, bookID);
    }

}