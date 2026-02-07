package assignment.java5.ass_self.Entities;

import assignment.java5.ass_self.enums.AuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull()
    @NotBlank(message = "Tên không được để trống")
    @Column(name = "FullName", nullable = false, length = 50)
    private String fullName;

    @Size(max = 50)
    @NotNull
    @Column(name = "Email", nullable = false, length = 50)
    private String email;

    @Size(max = 15)
    @Column(name = "Phone", nullable = false, length = 15)
    private String phone;

    @Size(max = 255)
    @Nationalized
    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Size(max = 10)
    @Column(name = "Gender", length = 10)
    private String gender;

    @Size(max = 255)
    @NotNull
    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @ColumnDefault("1")
    @Column(name = "IsActive")
    private Boolean isActive;

    @ColumnDefault("0")
    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "Provider", length = 20, nullable = false)
    private AuthProvider provider;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private Instant createdAt;


    @OneToMany(mappedBy = "userID", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<Cart> carts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<Notification> notifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<UserAddress> userAddresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<VoucherUsage> voucherUsages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Wishlist> wishlists = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<EmailVerificationToken> emailVerificationTokens = new LinkedHashSet<>();

}