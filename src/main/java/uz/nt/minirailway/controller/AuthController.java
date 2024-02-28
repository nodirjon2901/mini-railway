package uz.nt.minirailway.controller;

import uz.nt.minirailway.domain.dto.UserDto;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import uz.nt.minirailway.service.train.TrainService;
import uz.nt.minirailway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class AuthController {
    public static UserEntity currentUser;

    private final TrainService trainService;
    private final UserService userService;
    @GetMapping
    public String registerGet(Model model){
        return "auth";
    }
    @PostMapping
    public String registerPost(@ModelAttribute UserDto userDto,
                               Model model){
        userService.save(userDto);
        currentUser = userService.login(userDto.getUsername(), userDto.getPassword());
        if (!currentUser.getUsername().equals("admin")){
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allTrains", trainService.getAll());
            model.addAttribute("getArrivalTime", new HashMap<>());
            model.addAttribute("availableSeats",trainService.emptyTrainSeats());
            return "user-menu";
        } else {
            model.addAttribute("allTrains", trainService.getAll());
            model.addAttribute("getArrivalTime", new HashMap<>());
            model.addAttribute("availableSeats",trainService.emptyTrainSeats());
            return "admin-trains";
        }
    }

    @PostMapping(value = "/login")
    public String loginPost(@RequestParam(name = "username") String username,
                            @RequestParam(name = "password") String password,
                            Model model){
        currentUser = userService.login(username, password);
        if (!currentUser.getUsername().equals("admin")){
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allTrains", trainService.getAll());
            model.addAttribute("getArrivalTime", new HashMap<>());
            model.addAttribute("availableSeats",trainService.emptyTrainSeats());
            return "user-menu";
        } else {
            model.addAttribute("allTrains", trainService.getAll());
            model.addAttribute("getArrivalTime", new HashMap<>());
            model.addAttribute("availableSeats",trainService.emptyTrainSeats());
            return "admin-trains";
        }
    }
}
