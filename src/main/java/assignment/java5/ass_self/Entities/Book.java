package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookID", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Title", nullable = false)
    private String title;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Slug", nullable = false)
    private String slug;

    @Size(max = 20)
    @Column(name = "ISBN", length = 20)
    private String isbn;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @NotNull
    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PublisherID")
    private Publisher publisherID;

    @Column(name = "PublishYear")
    private Integer publishYear;

    @Size(max = 50)
    @Nationalized
    @Column(name = "\"Language\"", length = 50)
    private String language;

    @ColumnDefault("0")
    @Column(name = "SoldCount")
    private Integer soldCount;

    @ColumnDefault("1")
    @Column(name = "IsActive")
    private Boolean isActive;

    @ColumnDefault("0")
    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @ColumnDefault("sysdatetime()")
    @Column(name = "CreatedAt")
    private Instant createdAt;

    @OneToMany(mappedBy = "bookID")
    private Set<BookCategory> bookCategories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bookID")
    private Set<BookAuthor> bookAuthors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bookID")
    private Set<PromotionBook> promotionBooks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bookID")
    private Set<BookImage> bookImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bookID")
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    @OneToOne(mappedBy = "bookID")
    private Inventory inventory;

    @OneToMany(mappedBy = "bookID")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bookID")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bookID")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

    @Transient
    public BookImage getMainImage() {
        return this.bookImages.stream()
                .filter(BookImage::getIsMain)
                .findFirst()
                .orElse(null);
    }

    @Transient
    public Author getAuthors() {
        return this.bookAuthors.stream()
                .map(BookAuthor::getAuthorID)
                .findFirst()
                .orElse(null);
    }
    @Transient
    public @Size(max = 150) @NotNull String getPublisher() {
        return this.publisherID.getPublisherName();
    }
}