package ro.itschool.scheduler;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.itschool.controller.model.Ticket;
import ro.itschool.entity.WinningCombination;
import ro.itschool.repository.WinningCombinationRepo;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Log
public class InsertNumbersScheduler {

    @Autowired
    WinningCombinationRepo repo;

    //<second> <minute> <hour> <day-of-month> <month> <day-of-week>
    //0/x = every x seconds
    // – (range) => 0-5 seconds 0 to 5
    // https://www.baeldung.com/cron-expressions
    @Scheduled(cron = "0 0/1 * * * ?")
    public void insertWinningCombination() {
        Random random = new Random();

        Set<Integer> winningNumbers = new HashSet<>();
        while (winningNumbers.size() < 6) {
            winningNumbers.add(random.nextInt(1, 50));
        }

        StringBuilder winningString = new StringBuilder();
        for (Integer number : winningNumbers) {
            winningString.append(number).append(",");
        }


        final WinningCombination winningCombination = new WinningCombination();
        winningCombination.setInsertTime(LocalDateTime.now());
        winningCombination.setWinningNumbers(winningString.substring(0, winningString.length() - 1));

        repo.save(winningCombination);
        log.info("Insert winning combination: " + winningCombination);

        //Get all combinations from the other app (if not obsolete)
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Ticket>> tickets
                = restTemplate.exchange(
                "http://localhost:8080/ticket/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Ticket>>() {
                });

        log.info("All tickets: " + tickets.getBody());

        //Verify if any of those is a winning combination


        //Save the winner in database


        //Mark all combinations as obsolete
        ResponseEntity<Void> markAllObsolete
                = restTemplate.exchange(
                "http://localhost:8080/ticket/mark-obsolete",
                HttpMethod.POST,
                null,
                Void.class);


    }
}
