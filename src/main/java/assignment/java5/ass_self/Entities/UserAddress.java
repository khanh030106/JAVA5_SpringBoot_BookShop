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
@Table(name = "UserAddresses")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AddressID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Size(max = 50)
    @Column(name = "ReceiverName", length = 50)
    private String receiverName;

    @Size(max = 255)
    @Nationalized
    @Column(name = "AddressLine")
    private String addressLine;

    @Size(max = 100)
    @Nationalized
    @Column(name = "Province", length = 100)
    private String province;

    @Size(max = 100)
    @Nationalized
    @Column(name = "District", length = 100)
    private String district;

    @Size(max = 100)
    @Nationalized
    @Column(name = "Ward", length = 100)
    private String ward;

    @Size(max = 50)
    @Nationalized
    @ColumnDefault("Home")
    @Column(name = "AddressType", length = 50)
    private String addressType;

    @Size(max = 15)
    @Column(name = "Phone", length = 15)
    private String phone;

    @ColumnDefault("0")
    @Column(name = "IsDefault")
    private Boolean isDefault;

    @ColumnDefault("0")
    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "addressID")
    private Set<Order> orders = new LinkedHashSet<>();

}