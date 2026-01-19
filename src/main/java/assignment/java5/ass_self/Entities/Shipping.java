package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Shipping")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShippingID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OrderID", nullable = false)
    private Order orderID;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ShippingProvider", length = 100)
    private String shippingProvider;

    @Size(max = 150)
    @Nationalized
    @Column(name = "TrackingNumber", length = 150)
    private String trackingNumber;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ShippingStatus", length = 50)
    private String shippingStatus;

    @Column(name = "EstimatedDeliveryDate")
    private Instant estimatedDeliveryDate;

    @Column(name = "ActualDeliveryDate")
    private Instant actualDeliveryDate;

    @Column(name = "ShippedAt")
    private Instant shippedAt;

}