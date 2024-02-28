package uz.nt.minirailway.domain.dto;

import uz.nt.minirailway.domain.entity.train.DestinationPoint;
import uz.nt.minirailway.domain.entity.train.TrainClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainDto {
    private String name;
    private Double price;
    private LocalDateTime departure;
    private DestinationPoint startPoint;
    private DestinationPoint endPoint;
    private TrainClass trainClass;
}
