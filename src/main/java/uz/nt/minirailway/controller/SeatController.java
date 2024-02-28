package uz.nt.minirailway.controller;

import uz.nt.minirailway.domain.entity.seat.SeatEntity;
import uz.nt.minirailway.domain.entity.train.TrainEntity;
import uz.nt.minirailway.service.seat.SeatService;
import uz.nt.minirailway.service.train.TrainService;
import uz.nt.minirailway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static uz.nt.minirailway.controller.AuthController.currentUser;

@Controller
@RequestMapping(value = "/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;
    private final TrainService trainService;
    private final UserService userService;

    @GetMapping(value = "/view-seat/{seatId}")
    public String viewSeat(@PathVariable(value = "seatId")UUID seatId,
                           Model model){
        currentUser = userService.getById(currentUser.getId());
        SeatEntity seat = seatService.getById(seatId);
        model.addAttribute("seat", seat);
        model.addAttribute("currentUser", currentUser);
        return "book-seat";
    }

    @PostMapping(value = "/book-seat")
    public String buyTicket(@RequestParam(name = "fullName") String passengerName,
                            @RequestParam(name = "seatId") UUID seatId,
                            Model model){
        System.out.println(currentUser);
        seatService.bookSeat(currentUser, passengerName, seatId);
        SeatEntity seat = seatService.getById(seatId);
        TrainEntity train = trainService.getById(seat.getTrain().getId());
        userService.fillBalance(currentUser.getBalance() - seat.getTrain().getPrice(), currentUser.getId());
        currentUser.setBalance(currentUser.getBalance() - seat.getTrain().getPrice());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("train",seat.getTrain());
        model.addAttribute("availableSeats",seatService.emptySeats(train.getId()));
        model.addAttribute("getArrivalTime",trainService.getArrivalTime(train.getEndPoint()).get(train.getId()));
        model.addAttribute("message", "Ticket successfully bought!");
        return "train-seats";
    }

    @GetMapping(value = "/my-seats/{userId}")
    public String myTicketList(@PathVariable(value = "userId")UUID userId,
                               Model model){
        List<SeatEntity> seatEntities = seatService.myTicketList(userId);
        model.addAttribute("myTicketList", seatEntities);
        return "book-seat";
    }

}
