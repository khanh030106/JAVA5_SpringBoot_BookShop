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
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AddressID", nullable = false)
    private UserAddress addressID;

    @Column(name = "TotalAmount", precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 50)
    @Nationalized
    @Column(name = "CurrentStatus", length = 50)
    private String currentStatus;

    @Size(max = 255)
    @Nationalized
    @Column(name = "CanceledReason")
    private String canceledReason;

    @Column(name = "CanceledAt")
    private Instant canceledAt;

    @ColumnDefault("sysdatetime()")
    @Column(name = "CreatedAt")
    private Instant createdAt;

    @Column(name = "CompletedAt")
    private Instant completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VoucherID")
    private Voucher voucherID;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @ColumnDefault("0")
    @Column(name = "ShippingFee", precision = 18, scale = 2)
    private BigDecimal shippingFee;

    @ColumnDefault("0")
    @Column(name = "DiscountAmount", precision = 18, scale = 2)
    private BigDecimal discountAmount;

    @Size(max = 255)
    @Nationalized
    @Column(name = "Note")
    private String note;

    @OneToMany(mappedBy = "orderID")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderID")
    private Set<OrderStatusHistory> orderStatusHistories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderID")
    private Set<Payment> payments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderID")
    private Set<Shipping> shippings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderID")
    private Set<VoucherUsage> voucherUsages = new LinkedHashSet<>();

}