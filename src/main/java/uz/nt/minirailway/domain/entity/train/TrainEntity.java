package uz.nt.minirailway.domain.entity.train;

import uz.nt.minirailway.domain.entity.BaseEntity;
import uz.nt.minirailway.domain.entity.seat.SeatEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "trains")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TrainEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Positive()
    private Double price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "train", orphanRemoval = true)
    @OrderBy("seatNumber ASC")
    private List<SeatEntity> seats;

    @Column(nullable = false)
    private LocalDateTime departure;

    @Enumerated(EnumType.STRING)
    private DestinationPoint startPoint;

    @Enumerated(EnumType.STRING)
    private DestinationPoint endPoint;

    @Enumerated(EnumType.STRING)
    TrainClass trainClass;
}
