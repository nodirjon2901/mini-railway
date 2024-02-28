package uz.nt.minirailway.controller;

import uz.nt.minirailway.domain.entity.train.DestinationPoint;
import uz.nt.minirailway.domain.entity.train.TrainEntity;
import uz.nt.minirailway.service.seat.SeatService;
import uz.nt.minirailway.service.train.TrainService;
import uz.nt.minirailway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static uz.nt.minirailway.controller.AuthController.currentUser;

@Controller
@RequestMapping("/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    private final SeatService seatService;
    private final UserService userService;

    @GetMapping("/all")
    public String allTrains(Model model) {
        currentUser = userService.getById(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("availableSeats",trainService.emptyTrainSeats());
        model.addAttribute("allTrains", trainService.getAll());
        model.addAttribute("getArrivalTime", new HashMap<>());
        if (currentUser.getUsername().equals("admin")){
            return "admin-trains";
        }
        return "user-menu";
    }

    @GetMapping(value = "/train-edit/{trainId}")
    public String details(@PathVariable(value = "trainId") UUID trainId,Model model){
        TrainEntity train = trainService.getById(trainId);
        currentUser = userService.getById(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("train",train);
        model.addAttribute("availableSeats",seatService.emptySeats(train.getId()));
        model.addAttribute("getArrivalTime",trainService.getArrivalTime(train.getEndPoint()).get(trainId));
        return "train-seats";
    }

    @PostMapping(value = "/search")
    public String searchTrain(@RequestParam(name = "start") String start,
                              @RequestParam(name = "end") String end,
                              @RequestParam(name = "class") String trainClass,
                              @RequestParam(name = "time") LocalDateTime time,
                              Model model){
        List<TrainEntity> searchTrains = trainService.searchTrain(time, start, end, trainClass);
        if (searchTrains.size() == 0){
            model.addAttribute("message", "No any matching trains were found!");
        }
        currentUser = userService.getById(currentUser.getId());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("availableSeats",trainService.emptyTrainSeats());
        model.addAttribute("allTrains", trainService.getAll());
        model.addAttribute("getArrivalTime", trainService.getArrivalTime(DestinationPoint.valueOf(end)));
        model.addAttribute("searchTrains", searchTrains);
        return "search-user-trains";
    }
}
