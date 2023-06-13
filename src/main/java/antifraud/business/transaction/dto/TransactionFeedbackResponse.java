package antifraud.business.transaction.dto;

import antifraud.business.transaction.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class TransactionFeedbackResponse {

    private long transactionId;

    private long amount;

    private String ip;

    private String number;

    private String region;

    private String date;

    private String result;

    private String feedback;

    public TransactionFeedbackResponse(Transaction t) {
        this.feedback = t.getFeedback();
        this.date = t.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.ip = t.getIp();
        this.number = t.getNumber();
        this.transactionId = t.getId();
        this.region = t.getRegion();
        this.result = t.getResult();
        this.amount = t.getAmount();
    }

    public static TransactionFeedbackResponse fromEntity(Transaction t) {
        return new TransactionFeedbackResponse(t);
    }

}
