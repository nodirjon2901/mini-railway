package uz.nt.minirailway.config;

import uz.nt.minirailway.exception.AlreadyExistsException;
import uz.nt.minirailway.exception.NotFoundException;
import uz.nt.minirailway.exception.WrongSearchException;
import uz.nt.minirailway.service.train.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

import static uz.nt.minirailway.controller.AuthController.currentUser;

@ControllerAdvice
@RequiredArgsConstructor
public class RailwayExceptionHandler extends ResponseEntityExceptionHandler {
    private final TrainService trainService;
    @ExceptionHandler(value = AlreadyExistsException.class)
    public String handleDataNotFoundException(AlreadyExistsException e,
                                              Model model){
        model.addAttribute("message", e.getMessage());
        return "auth";
    }
    @ExceptionHandler(value = NotFoundException.class)
    public String handleDataNotFoundException(NotFoundException e,
                                              Model model){
        model.addAttribute("message", e.getMessage());
        return "auth";
    }
    @ExceptionHandler(value = WrongSearchException.class)
    public String handleWrongSearchException(WrongSearchException e,
                                              Model model){
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("availableSeats",trainService.emptyTrainSeats());
        model.addAttribute("allTrains", trainService.getAll());
        model.addAttribute("getArrivalTime", new HashMap<>());
        model.addAttribute("message", "Wrong search input please try again!");
        return "user-menu";
    }
}
