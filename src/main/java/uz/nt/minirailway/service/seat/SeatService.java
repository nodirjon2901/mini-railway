package uz.nt.minirailway.service.seat;

import uz.nt.minirailway.domain.dto.SeatDto;
import uz.nt.minirailway.domain.entity.seat.SeatEntity;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import uz.nt.minirailway.exception.AlreadyExistsException;
import uz.nt.minirailway.exception.NotFoundException;
import uz.nt.minirailway.repository.SeatRepository;
import uz.nt.minirailway.service.BaseService;
import uz.nt.minirailway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SeatService implements BaseService<SeatDto, SeatEntity> {

    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    @Override
    public void save(SeatDto createDto) {
        SeatEntity seat=modelMapper.map(createDto, SeatEntity.class);
        try {
            seatRepository.save(seat);
        } catch (Exception e) {
            throw new AlreadyExistsException("Seat already exists");
        }
    }

    @Override
    public void delete(UUID id) {
        Optional<SeatEntity> byId = seatRepository.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException("This id was not found");
        } else seatRepository.deleteById(id);
    }

    @Override
    public void delete(SeatEntity seatToDelete) {
        if (seatToDelete != null) {
            seatRepository.delete(seatToDelete);
        } else throw new NotFoundException("This seat was not found");
    }

    @Override
    public void update(SeatDto createDto, UUID id) {

    }

    public void bookSeat(UserEntity user, String passengerName, UUID id) {
        seatRepository.bookSeat(user, passengerName, id);
    }

    @Override
    public SeatEntity getById(UUID id) {
        if (seatRepository.findById(id).isPresent()){
            return seatRepository.findById(id).get();
        } else {
            throw new NotFoundException("Seat not found!");
        }
    }

    @Override
    public List<SeatEntity> getAll() {
        return seatRepository.findAll();
    }

    public List<SeatEntity> getAllByTrainName(String trainName) {
        return seatRepository.findByTrainName(trainName);
    }

    public SeatEntity getByTrainIdAndSeatNumber(UUID trainId, int seatNum) {
        SeatEntity seat = seatRepository.findByTrainIdAndSeatNumber(trainId, seatNum);
        if (seat != null) {
            return seat;
        } else {
            throw new NotFoundException("Seat not found!");
        }
    }
    public List<SeatEntity> emptySeats(UUID trainId) {
        Iterator<SeatEntity> iterator = seatRepository.findByTrainId(trainId).iterator();
        List<SeatEntity> seats = new ArrayList<>();
        while (iterator.hasNext()) {
            SeatEntity emptySeat = iterator.next();
            if (emptySeat.getUser() == null) {
                seats.add(emptySeat);
            }
        }
        return seats;
    }


    public List<SeatEntity> reservedSeats(UUID trainId) {
        Iterator<SeatEntity> iterator = seatRepository.findByTrainId(trainId).iterator();
        List<SeatEntity> seats = new ArrayList<>();
        while (iterator.hasNext()) {
            SeatEntity reservedSeat = iterator.next();
            if (reservedSeat.getUser() != null) {
                seats.add(reservedSeat);
            }
        }
        return seats;
    }
    public void deleteTicket(UUID ticketId){
        SeatEntity seat = seatRepository.findById(ticketId).
                orElseThrow(() -> new NotFoundException("Seat not found!"));
        try{
            Double balance = seat.getUser().getBalance() + (seat.getTrain().getPrice() * 0.5);
            userService.fillBalance(balance, seat.getUser().getId());
            seat.setPassengerName(null);
            seat.setUser(null);
            seatRepository.save(seat);
        }catch (Exception e){
            throw new NotFoundException("This seat does not exists");
        }
    }

    public List<SeatEntity> myTicketList(UUID userId){
        List<SeatEntity> myTicketList=new ArrayList<>();
        for (SeatEntity seat: seatRepository.findAll()){
            if (seat.getUser() != null && Objects.equals(seat.getUser().getId(),userId)){
                myTicketList.add(seat);
            }
        }
        return myTicketList;
    }
}
