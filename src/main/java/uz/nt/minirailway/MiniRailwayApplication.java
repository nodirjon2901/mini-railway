package uz.nt.minirailway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.nt.minirailway.domain.entity.user.UserEntity;
import uz.nt.minirailway.repository.UserRepository;

import java.util.List;

@SpringBootApplication
public class MiniRailwayApplication {

    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MiniRailwayApplication.class, args);
    }

}
