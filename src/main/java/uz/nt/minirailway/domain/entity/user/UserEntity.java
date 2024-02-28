package uz.nt.minirailway.domain.entity.user;

import uz.nt.minirailway.domain.entity.BaseEntity;
import uz.nt.minirailway.domain.entity.seat.SeatEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity extends BaseEntity {
    @Column(nullable = false)
    @NotBlank
    private String fullName;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false)
    @Positive
    private Double balance = 1.0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<SeatEntity> seats = new ArrayList<>();
}
