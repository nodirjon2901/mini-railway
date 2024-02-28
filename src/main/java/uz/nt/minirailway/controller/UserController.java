package uz.nt.minirailway.controller;

import uz.nt.minirailway.service.seat.SeatService;
import uz.nt.minirailway.service.train.TrainService;
import uz.nt.minirailway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.UUID;

import static uz.nt.minirailway.controller.AuthController.currentUser;

@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SeatService seatService;
    private final TrainService trainService;
    @PostMapping(value = "/fill-balance")
    public String loginPost(@RequestParam(name = "amount") Double amount,
                            @RequestParam(name = "userId") UUID userId,
                            Model model){
        Double updatedBalance = currentUser.getBalance() + amount;
        userService.fillBalance(updatedBalance, userId);
        currentUser = userService.getById(userId);
        model.addAttribute("getArrivalTime", new HashMap<>());
        model.addAttribute("allTrains", trainService.getAll());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("availableSeats",trainService.emptyTrainSeats());
        model.addAttribute("message", "Balance successfully filled!");
        return "user-menu";
    }
    @GetMapping(value = "/my-tickets")
    public String myTickets(Model model){
        currentUser = userService.getById(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("myTickets", seatService.myTicketList(currentUser.getId()));
        return "my-tickets";
    }

    @PostMapping(value = "/delete-my-ticket")
    public String deleteMyTicket(@RequestParam(name = "deleteId") UUID ticketId,
                                 Model model){
        seatService.deleteTicket(ticketId);
        currentUser = userService.getById(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("myTickets", seatService.myTicketList(currentUser.getId()));
        model.addAttribute("message", "Ticket deleted!");
        return "my-tickets";
    }
}
