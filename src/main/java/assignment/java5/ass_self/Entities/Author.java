package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuthorID", nullable = false)
    private Long id;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "AuthorName", nullable = false, length = 150)
    private String authorName;

    @Nationalized
    @Lob
    @Column(name = "Biography")
    private String biography;

    @ColumnDefault("0")
    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "authorID")
    private Set<BookAuthor> bookAuthors = new LinkedHashSet<>();

}