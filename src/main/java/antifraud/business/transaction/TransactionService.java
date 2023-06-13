package antifraud.business.transaction;

import antifraud.business.stolencard.CardService;
import antifraud.business.transaction.dto.TransactionFeedbackResponse;
import antifraud.exception.NotFoundException;
import antifraud.exception.WrongFormatException;
import antifraud.persistence.TransactionRepository;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.*;

@Service
public class TransactionService {

    TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    private static long allowanceLimit = 200;

    private static long prohibitionLimit = 1500;

    public enum Status {
        ALLOWED,
        MANUAL_PROCESSING,
        PROHIBITED,
    }

    public String getStatus(long amount) {
        if (amount <= 0) {
            throw new WrongFormatException();
        } else if (amount <= allowanceLimit) {
            return Status.ALLOWED.name();
        } else if (amount <= prohibitionLimit) {
            return Status.MANUAL_PROCESSING.name();
        } else {
            return Status.PROHIBITED.name();
        }
    }

    public static void setAllowanceLimit(long amount, boolean trend) {
        allowanceLimit = (long) Math.ceil(allowanceLimit * 0.8 + 0.2 * amount * (trend ? 1 : (-1)));
    }

    public static void setProhibitionLimit(long amount, boolean trend) {
        prohibitionLimit = (long) Math.ceil(prohibitionLimit * 0.8 + 0.2 * amount * (trend ? 1 : (-1)));
    }

    public static long getAllowanceLimit() {
        return allowanceLimit;
    }

    public static long getProhibitionLimit() {
        return prohibitionLimit;
    }

    public List<Transaction> getAllInHour(LocalDateTime first, LocalDateTime second) {
        return transactionRepository.findAllByDateGreaterThanEqualAndDateLessThanEqual(first, second);
    }

    public Optional<Transaction> findById(long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getHistoryForNumber(String number) {
        if (!CardService.isValidLuhn(number)) {
            throw new WrongFormatException();
        }
        List<Transaction> list = transactionRepository.findAllByNumber(number);
        if (list.size() == 0) {
            throw new NotFoundException();
        }
        return list;
    }

    public List<Transaction> getHistory() {
        return (List<Transaction>) transactionRepository.findAll();
    }
}