package ro.itschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.itschool.entity.WinningCombination;
import ro.itschool.repository.WinningCombinationRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class WinningCombinationRestController {

    @Autowired
    private WinningCombinationRepo repo;

    @GetMapping(value = "/winning-combination")
    public List<Integer> getWiningCombination() {
        Optional<WinningCombination> latestWinningCombination = repo.getLatestWinningCombination();

        return latestWinningCombination
                .map(winningCombination -> Arrays.stream(winningCombination.getWinningNumbers().split(","))
                        .map(Integer::parseInt)
                        .toList())
                .orElseGet(ArrayList::new);
    }
}
