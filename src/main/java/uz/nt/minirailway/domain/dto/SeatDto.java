package uz.nt.minirailway.domain.dto;


import uz.nt.minirailway.domain.entity.train.TrainEntity;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatDto {
    private UserEntity user;
    private String passengerName;
    private int seatNumber;
    private TrainEntity train;
}
