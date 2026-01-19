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
@Table(name = "Vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VoucherID", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "Code", nullable = false, length = 50)
    private String code;

    @Size(max = 255)
    @Nationalized
    @Column(name = "Description")
    private String description;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "DiscountType", nullable = false, length = 20)
    private String discountType;

    @NotNull
    @Column(name = "DiscountValue", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountValue;

    @ColumnDefault("0")
    @Column(name = "MinOrderAmount", precision = 18, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(name = "MaxDiscountAmount", precision = 18, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "UsageLimit")
    private Integer usageLimit;

    @ColumnDefault("0")
    @Column(name = "UsedCount")
    private Integer usedCount;

    @NotNull
    @Column(name = "StartDate", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "EndDate", nullable = false)
    private Instant endDate;

    @ColumnDefault("1")
    @Column(name = "IsActive")
    private Boolean isActive;

    @ColumnDefault("sysdatetime()")
    @Column(name = "CreatedAt")
    private Instant createdAt;

    @OneToMany(mappedBy = "voucherID")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "voucherID")
    private Set<VoucherUsage> voucherUsages = new LinkedHashSet<>();

}