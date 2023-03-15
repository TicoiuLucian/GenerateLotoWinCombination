package ro.itschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WinningLotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinningLotoApplication.class, args);
    }

}
