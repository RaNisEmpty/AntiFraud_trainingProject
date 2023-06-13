package antifraud.presentation;

import antifraud.business.stolencard.CardService;
import antifraud.business.susip.SuspiciousIpService;
import antifraud.business.transaction.Transaction;
import antifraud.business.transaction.TransactionService;
import antifraud.business.transaction.dto.TransactionFeedbackRequest;
import antifraud.business.transaction.dto.TransactionFeedbackResponse;
import antifraud.business.transaction.dto.TransactionRequest;
import antifraud.business.transaction.dto.TransactionResponse;
import antifraud.exception.FeedbackAlreadySpecifiedException;
import antifraud.exception.NotFoundException;
import antifraud.exception.UnprocessableFeedbackException;
import antifraud.exception.WrongFormatException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    private final SuspiciousIpService ipService;

    private final CardService cardService;

    @Autowired
    public TransactionController(TransactionService transactionService, CardService cardService, SuspiciousIpService ipService) {
        this.transactionService = transactionService;
        this.cardService = cardService;
        this.ipService = ipService;
    }

    @PostMapping("/api/antifraud/transaction")
    public TransactionResponse antifraud(@Valid @RequestBody TransactionRequest transactionRequest) {

        String result = transactionService.getStatus(transactionRequest.getAmount());
        List<String> prohibitionReason = new ArrayList<>();
        List<String> manualReason = new ArrayList<>();
        LocalDateTime current = LocalDateTime.parse(transactionRequest.getDate());
        if (result.equals(TransactionService.Status.PROHIBITED.name())) {
            prohibitionReason.add("amount");
        } else if (result.equals(TransactionService.Status.MANUAL_PROCESSING.name())) {
            manualReason.add("amount");
        }
        if (cardService.existsByNumber(transactionRequest.getNumber())) {
            prohibitionReason.add("card-number");
        }
        if (ipService.existsByIp(transactionRequest.getIp())) {
            prohibitionReason.add("ip");
        }
        List<Transaction> toCheck = transactionService.getAllInHour(current.minusHours(1L), current);
        Set<String> regions = new HashSet<>();
        Set<String> ips = new HashSet<>();
        for(Transaction t: toCheck) {
            if (!t.getRegion().equals(transactionRequest.getRegion())) {
                regions.add(t.getRegion());
            }
            if (!t.getIp().equals(transactionRequest.getIp())) {
                ips.add(t.getIp());
            }
        }

        if (ips.size() >= 3) { //ip counter
            prohibitionReason.add("ip-correlation");
        } else if (ips.size() == 2) {
            manualReason.add("ip-correlation");
        }

        if (regions.size() >= 3) { //region counter
            prohibitionReason.add("region-correlation");
        } else if (regions.size() == 2) {
            manualReason.add("region-correlation");
        }



        Transaction trans = new Transaction();
        trans.setAmount(transactionRequest.getAmount());
        trans.setIp(transactionRequest.getIp());
        trans.setNumber(transactionRequest.getNumber());
        trans.setRegion(transactionRequest.getRegion());
        trans.setDate(LocalDateTime.parse(transactionRequest.getDate()));

        if (!prohibitionReason.isEmpty()) { //isProhibited
            trans.setResult(TransactionService.Status.PROHIBITED.name());
            trans.setInfo(StringUtils.join(prohibitionReason, ", "));
            transactionService.save(trans);
            return TransactionResponse.fromEntity(trans.getResult(), trans.getInfo());
        }

        if (!manualReason.isEmpty()) { //isManual
            trans.setResult(TransactionService.Status.MANUAL_PROCESSING.name());
            trans.setInfo(StringUtils.join(manualReason, ", "));
            transactionService.save(trans);
            return TransactionResponse.fromEntity(trans.getResult(), trans.getInfo());
        }

        trans.setResult(result);
        trans.setInfo("none");
        transactionService.save(trans);
        return TransactionResponse.fromEntity(result, trans.getInfo());
    }

    @PutMapping("/api/antifraud/transaction")
    public TransactionFeedbackResponse setFeedback(@RequestBody TransactionFeedbackRequest transactionFeedbackRequest) {
        boolean tmp = false;
        for (var i: TransactionService.Status.values()) {
            if (transactionFeedbackRequest.getFeedback().equals(i.toString())) {
                tmp = true;
                break;
            }
        }
        if (!tmp) {
            throw new WrongFormatException();
        }
        Optional<Transaction> t = transactionService.findById(transactionFeedbackRequest.getTransactionId());
        if (t.isEmpty()) {
            throw new NotFoundException();
        }
        if (!t.get().getFeedback().equals("")) {
            throw new FeedbackAlreadySpecifiedException();
        }
        if (t.get().getResult().equals(transactionFeedbackRequest.getFeedback())) {
            throw new UnprocessableFeedbackException();
        }

        Transaction trans = t.get();
        long a = trans.getAmount();
        if (trans.getResult().equals(TransactionService.Status.PROHIBITED.toString())) {
            if (transactionFeedbackRequest.getFeedback().equals(TransactionService.Status.MANUAL_PROCESSING.toString())) {
                TransactionService.setProhibitionLimit(a, true);
            } else {
                TransactionService.setProhibitionLimit(a, true);
                TransactionService.setAllowanceLimit(a, true);
            }
        } else if (trans.getResult().equals(TransactionService.Status.MANUAL_PROCESSING.toString())) {
            if (transactionFeedbackRequest.getFeedback().equals(TransactionService.Status.ALLOWED.toString())) {
                TransactionService.setAllowanceLimit(a, true);
            } else {
                TransactionService.setProhibitionLimit(a, false);
            }
        } else {
            if (transactionFeedbackRequest.getFeedback().equals(TransactionService.Status.MANUAL_PROCESSING.toString())) {
                TransactionService.setAllowanceLimit(a, false);
            } else {
                TransactionService.setAllowanceLimit(a, false);
                TransactionService.setProhibitionLimit(a, false);
            }
        }

        trans.setFeedback(transactionFeedbackRequest.getFeedback());
        transactionService.save(trans);
        return TransactionFeedbackResponse.fromEntity(trans);
    }

    @GetMapping("/api/antifraud/history")
    public List<TransactionFeedbackResponse> getHistory() {
        return transactionService.getHistory().stream().map(TransactionFeedbackResponse::fromEntity).toList();
    }

    @GetMapping("/api/antifraud/history/{number}")
    public List<TransactionFeedbackResponse> getHistoryForNumber(@PathVariable String number) {
        return transactionService.getHistoryForNumber(number).stream().map(TransactionFeedbackResponse::fromEntity).toList();
    }
}