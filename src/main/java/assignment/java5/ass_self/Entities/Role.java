package assignment.java5.ass_self.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "RoleName", nullable = false, length = 50)
    private String roleName;

    @OneToMany(mappedBy = "roleID")
    private Set<UserRole> userRoles = new LinkedHashSet<>();

}