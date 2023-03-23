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
import ro.itschool.entity.Winner;
import ro.itschool.entity.WinningCombination;
import ro.itschool.repository.WinnerRepo;
import ro.itschool.repository.WinningCombinationRepo;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log
public class InsertNumbersScheduler {

    @Autowired
    WinningCombinationRepo repo;

    @Autowired
    WinnerRepo winnerRepo;

    int counter = 0;

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

        //TODO Verify if any of those is a winning combination


        //TODO Save the winner in database


        Set<Integer> parseWinningNumbers = Arrays.stream(winningCombination.getWinningNumbers().split(","))
          .map(Integer::parseInt)
          .collect(Collectors.toSet());

        for (Ticket ticket : Objects.requireNonNull(tickets.getBody())) {
            Set<Integer> parseEachTicketNumbers = Arrays.stream(ticket.getWinningNumbers().split(","))
              .map(Integer::parseInt)
              .collect(Collectors.toSet());
            for (Integer wn : parseWinningNumbers) {
                for (Integer number : parseEachTicketNumbers) {
                    if (Objects.equals(wn, number)) {
                        counter++;
                    }
                    if (counter == parseWinningNumbers.size()) {
                        Winner winner = new Winner();
                        winner.setWinnerName(ticket.getBuyer());
                        winner.setWinningTime(winningCombination.getInsertTime());
                        winnerRepo.save(winner);
                        log.info("New winner: " + winner);
                    }
                }
            }
        }


        //Mark all combinations as obsolete
        ResponseEntity<Void> markAllObsolete
                = restTemplate.exchange(
                "http://localhost:8080/ticket/mark-obsolete",
                HttpMethod.POST,
                null,
                Void.class);


    }
}
