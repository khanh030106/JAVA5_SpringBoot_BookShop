package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PromotionID", nullable = false)
    private Long id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "PromotionName", length = 50)
    private String promotionName;

    @Column(name = "DiscountPercent")
    private Integer discountPercent;

    @NotNull
    @Column(name = "StartDate", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "EndDate", nullable = false)
    private Instant endDate;

    @OneToMany(mappedBy = "promotionID")
    private Set<PromotionBook> promotionBooks = new LinkedHashSet<>();

}