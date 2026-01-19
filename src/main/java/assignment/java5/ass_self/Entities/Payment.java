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

@Getter
@Setter
@Entity
@Table(name = "Payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OrderID", nullable = false)
    private Order orderID;

    @Size(max = 100)
    @Nationalized
    @Column(name = "Provider", length = 100)
    private String provider;

    @Size(max = 150)
    @Nationalized
    @Column(name = "TransactionCode", length = 150)
    private String transactionCode;

    @NotNull
    @Column(name = "Amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("Pending")
    @Column(name = "Status", length = 50)
    private String status;

    @Nationalized
    @Lob
    @Column(name = "ResponseData")
    private String responseData;

    @ColumnDefault("sysdatetime()")
    @Column(name = "PaymentDate")
    private Instant paymentDate;

}