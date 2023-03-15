package ro.itschool.controller.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class Ticket {

    private Integer id;

    //14,24,45,2,8,33
    private String winningNumbers;

    private LocalDateTime insertTime;

    private String buyer;
}
