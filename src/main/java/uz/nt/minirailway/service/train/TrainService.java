package uz.nt.minirailway.service.train;

import uz.nt.minirailway.domain.dto.SeatDto;
import uz.nt.minirailway.domain.dto.TrainDto;
import uz.nt.minirailway.domain.entity.seat.SeatEntity;
import uz.nt.minirailway.domain.entity.train.DestinationPoint;
import uz.nt.minirailway.domain.entity.train.TrainClass;
import uz.nt.minirailway.domain.entity.train.TrainEntity;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import uz.nt.minirailway.exception.AlreadyExistsException;
import uz.nt.minirailway.exception.NotFoundException;
import uz.nt.minirailway.exception.WrongSearchException;
import uz.nt.minirailway.repository.TrainRepository;
import uz.nt.minirailway.service.BaseService;
import uz.nt.minirailway.service.seat.SeatService;
import uz.nt.minirailway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainService implements BaseService<TrainDto, TrainEntity> {

    private final TrainRepository trainRepository;

    private final SeatService seatService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Override
    public void save(TrainDto createDto) {
        TrainEntity train = modelMapper.map(createDto, TrainEntity.class);
        List<SeatEntity> seats = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            SeatDto seatDto = new SeatDto(null, null, i, train);
            seats.add(modelMapper.map(seatDto, SeatEntity.class));
        }
        train.setSeats(seats);
        try {
            trainRepository.save(train);
        }catch (Exception e){
            throw new AlreadyExistsException("Same name already exists!");
        }
    }

    @Override
    public void delete(UUID id) {
        trainRepository.deleteByTrainId(id);
    }

    @Override
    public void delete(TrainEntity trainEntity) {
        Double price = trainEntity.getPrice();
        for (SeatEntity seat : trainEntity.getSeats()) {
            if (seat.getUser() != null){
                UserEntity user = seat.getUser();
                userService.fillBalance(user.getBalance() + price, user.getId());
            }
        }
        trainRepository.delete(trainEntity);
    }

    @Override
    public void update(TrainDto createDto, UUID id) {

    }

    //
    public void update(String name, Double price, UUID trainId){
        TrainEntity train = trainRepository.findById(trainId).
                orElseThrow(() -> new NotFoundException("Train not found!"));
        try{
            train.setName(name);
            train.setPrice(price);
            trainRepository.save(train);
        }catch (Exception e){
            throw new NotFoundException("This train does not exists");
        }
    }

    @Override
    public TrainEntity getById(UUID id) {
        if (trainRepository.findById(id).isPresent()){
            return trainRepository.findById(id).get();
        } else {
            throw new NotFoundException("Train not found!");
        }
    }
    public HashMap<UUID, Integer> emptyTrainSeats(){
        HashMap<UUID, Integer> emptySeats = new HashMap<>();
        for (TrainEntity train : trainRepository.findAll()) {
            emptySeats.put(train.getId(), seatService.emptySeats(train.getId()).size());
        }
        return emptySeats;
    }

    public List<TrainEntity> trainByTime(LocalDateTime time){
        List<TrainEntity> trainByTime = new ArrayList<>();
        for (TrainEntity train : trainRepository.findAll()) {
            if (train.getDeparture().getDayOfYear() == time.getDayOfYear()){
                trainByTime.add(train);
            }
        }
        return trainByTime;
    }

    public List<TrainEntity> forwardDestinationTrains(LocalDateTime time,DestinationPoint start, DestinationPoint end, TrainClass trainClass){
        List<TrainEntity> forwardDestinationTrains = new ArrayList<>();
        for (TrainEntity train : trainByTime(time)) {
            if (train.getStartPoint().getValue() <= start.getValue() && train.getEndPoint().getValue() >= end.getValue() && train.getTrainClass().equals(trainClass)){
                forwardDestinationTrains.add(train);
            }
        }
        return forwardDestinationTrains;
    }

    public List<TrainEntity> reverseDestinationTrains(LocalDateTime time,DestinationPoint start, DestinationPoint end, TrainClass trainClass){
        List<TrainEntity> reverseDestinationTrains = new ArrayList<>();
        for (TrainEntity train : trainByTime(time)) {
            if (train.getStartPoint().getValue() >= start.getValue() && train.getEndPoint().getValue() <= end.getValue() && train.getTrainClass().equals(trainClass)){
                reverseDestinationTrains.add(train);
            }
        }
        return reverseDestinationTrains;
    }
    @Override
    public List<TrainEntity> getAll() {
        return trainRepository.findAll();
    }

    public HashMap<UUID, LocalDateTime> getArrivalTime(DestinationPoint endPoint){
        HashMap<UUID, LocalDateTime> getArrivalTime = new HashMap<>();
        for (TrainEntity trainEntity : trainRepository.findAll()) {
            int distance = Math.abs(trainEntity.getStartPoint().getValue() - endPoint.getValue());
            getArrivalTime.put(trainEntity.getId(), trainEntity.getDeparture().plusHours(distance));
        }
        return getArrivalTime;
    }

    public List<TrainEntity> searchTrain(LocalDateTime time,
                                         String start,
                                         String end,
                                         String trainClass){
        if (trainClass.equals("ALL") || start.equals("ALL") || end.equals("ALL") ||
                start.equals(end)){
            throw new WrongSearchException("Please enter correct values to search!");
        }
        DestinationPoint startPoint = DestinationPoint.valueOf(start);
        DestinationPoint endPoint = DestinationPoint.valueOf(end);
        TrainClass trainClassEnum = TrainClass.valueOf(trainClass);
        if (startPoint.getValue() < endPoint.getValue()){
            return forwardDestinationTrains(time, startPoint, endPoint, trainClassEnum);
        } else {
            return reverseDestinationTrains(time, startPoint, endPoint, trainClassEnum);
        }
    }
}
