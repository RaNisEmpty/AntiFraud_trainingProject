package antifraud.business.transaction.dto;

import antifraud.business.transaction.TransactionService;
import antifraud.exception.WrongFormatException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TransactionFeedbackRequest {

    private long transactionId;

    private String feedback;

    public TransactionFeedbackRequest(long transactionId, String feedback) {

        this.transactionId = transactionId;

        for (var i: TransactionService.Status.values()) {
            if (feedback.equals(i.toString())) {
                this.feedback = feedback;
                break;
            }
        }

        throw new WrongFormatException();

    }
}


