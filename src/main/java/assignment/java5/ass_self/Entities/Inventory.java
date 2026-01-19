package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "Inventories")
public class Inventory {
    @Id
    @Column(name = "BookID", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BookID", nullable = false)
    private Book bookID;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @ColumnDefault("0")
    @Column(name = "Reserved")
    private Integer reserved;

}