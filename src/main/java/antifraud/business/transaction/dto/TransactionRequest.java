package antifraud.business.transaction.dto;

import antifraud.business.stolencard.CardService;
import antifraud.business.transaction.Transaction;
import antifraud.exception.WrongFormatException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
public class TransactionRequest {

    private long amount;

    @NotEmpty(message = "IP should not be empty")
    private String ip;

    @NotEmpty(message = "Number should not be empty")
    private String number;

    @NotEmpty(message = "Region should not be empty")
    private String region;

    @NotEmpty(message = "Date should not be empty in json")
    private String date;

    public TransactionRequest(long amount, String ip, String number, String region, String date) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            throw new WrongFormatException();
        }
        if (CardService.isValidLuhn(number)) {
            this.number = number;
        } else {
            throw new WrongFormatException();
        }
        if (InetAddressValidator.getInstance().isValid(ip)) {
            this.ip = ip;
        } else {
            throw new WrongFormatException();
        }
        boolean tmp = false;
        for(var i: Transaction.possibleRegions) {
            if (i.equals(region)) {
                tmp = true;
                break;
            }
        }
        if (tmp) {
            this.region = region;
        } else {
            throw new WrongFormatException();
        }
        LocalDateTime.parse(date);
        this.date = date;
    }

}
