package antifraud.business.transaction.dto;

import antifraud.business.transaction.Transaction;

public record TransactionResponse(String result, String info) {
    public static TransactionResponse fromEntity(String result, String info) {
        return new TransactionResponse(result, info);
    }
}
