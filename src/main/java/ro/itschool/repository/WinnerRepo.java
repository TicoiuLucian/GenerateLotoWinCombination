package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.entity.Winner;

public interface WinnerRepo extends JpaRepository<Winner, Integer> {
}
