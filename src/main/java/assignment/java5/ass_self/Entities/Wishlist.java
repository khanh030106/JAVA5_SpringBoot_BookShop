package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Wishlists")
public class Wishlist {
    @EmbeddedId
    private WishlistId id;

    @MapsId("userID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @MapsId("bookID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookID", nullable = false)
    private Book book;

    @ColumnDefault("sysdatetime()")
    @Column(name = "CreatedAt")
    private Instant createdAt;

}