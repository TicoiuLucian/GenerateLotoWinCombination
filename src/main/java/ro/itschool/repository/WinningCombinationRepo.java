package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.itschool.entity.WinningCombination;

import java.util.List;
import java.util.Optional;

public interface WinningCombinationRepo extends JpaRepository<WinningCombination, Integer> {
    @Query(value = "SELECT * FROM Winning_Combination ORDER BY insert_time DESC LIMIT 1", nativeQuery = true)
    Optional<WinningCombination> getLatestWinningCombination();
}
