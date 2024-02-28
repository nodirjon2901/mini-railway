package uz.nt.minirailway.controller;

import uz.nt.minirailway.domain.dto.TrainDto;
import uz.nt.minirailway.domain.entity.train.TrainEntity;
import uz.nt.minirailway.service.seat.SeatService;
import uz.nt.minirailway.service.train.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TrainService trainService;
    private final SeatService seatService;
    @PostMapping(value = "/create-train")
    public String createTrains(@ModelAttribute TrainDto trainDto, Model model){
        trainService.save(trainDto);
        model.addAttribute("allTrains", trainService.getAll());
        model.addAttribute("getArrivalTime", new HashMap<>());
        model.addAttribute("availableSeats",trainService.emptyTrainSeats());
        model.addAttribute("message", "Train successfully added!");
        return "admin-trains";
    }
    @GetMapping(value = "/edit-train/{trainId}")
    public String editTrainGet(@PathVariable(name = "trainId")UUID trainId,
                               Model model){
        TrainEntity train = trainService.getById(trainId);
        model.addAttribute("train",train);
        model.addAttribute("bookedSeats", seatService.reservedSeats(trainId));
        model.addAttribute("availableSeats",seatService.emptySeats(train.getId()));
        model.addAttribute("getArrivalTime",trainService.getArrivalTime(train.getEndPoint()).get(trainId));
        return "edit-train";
    }
    @PostMapping(value = "/delete")
    public String deleteTrain(@RequestParam(name = "id") UUID trainId,
                              Model model){
        TrainEntity train = trainService.getById(trainId);
        trainService.delete(train);
        model.addAttribute("allTrains", trainService.getAll());
        model.addAttribute("getArrivalTime", new HashMap<>());
        model.addAttribute("availableSeats",trainService.emptyTrainSeats());
        model.addAttribute("message", "Train successfully deleted!");
        return "admin-trains";
    }

    @PostMapping(value = "/edit-train")
    public String editTrainPost(@RequestParam(name = "name") String name,
                                @RequestParam(name = "price") Double price,
                                @RequestParam(name = "id") UUID id,
                                Model model){
        trainService.update(name, price, id);
        TrainEntity train = trainService.getById(id);
        model.addAttribute("train",train);
        model.addAttribute("bookedSeats", seatService.reservedSeats(id));
        model.addAttribute("availableSeats",seatService.emptySeats(train.getId()));
        model.addAttribute("getArrivalTime",trainService.getArrivalTime(train.getEndPoint()));
        model.addAttribute("message", "Train successfully edited!");
        return "edit-train";
    }
}
