package antifraud.business.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "transactions")
public class Transaction {

    public static final List<String> possibleRegions = new ArrayList<>(List.of("EAP", "ECA", "HIC", "LAC", "MENA", "SA", "SSA"));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "Amount definitely should not be empty")
    private long amount;

    @NotEmpty(message = "IP should not be empty")
    private String ip;

    @NotEmpty(message = "Number should not be empty")
    private String number;

    @NotEmpty(message = "Region should not be empty")
    private String region;

    @Column(columnDefinition = "TIMESTAMP")
    @NotNull(message = "Date should not be empty")
    private LocalDateTime date;

    private String result;

    private String feedback = "";

    @JsonIgnore
    private String info;

}
