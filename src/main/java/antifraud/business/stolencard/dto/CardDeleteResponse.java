package antifraud.business.stolencard.dto;

import antifraud.business.stolencard.Card;

public record CardDeleteResponse(String status) {
    public static CardDeleteResponse fromEntity(Card card) {
        return new CardDeleteResponse("Card " + card.getNumber() + " successfully removed!");
    }
}
