package uz.nt.minirailway.domain.entity.seat;

import uz.nt.minirailway.domain.entity.BaseEntity;
import uz.nt.minirailway.domain.entity.train.TrainEntity;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity(name = "seats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SeatEntity extends BaseEntity {
    @ManyToOne(cascade = CascadeType.MERGE)
    private UserEntity user;

    private String passengerName;

    @Column(nullable = false)
    private int seatNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    private TrainEntity train;
}
