package antifraud.presentation;

import antifraud.business.stolencard.Card;
import antifraud.business.stolencard.CardService;
import antifraud.business.stolencard.dto.CardDeleteResponse;
import antifraud.business.stolencard.dto.CardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardController {

    CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<Card> postStolenCard(@Validated @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.save(cardRequest));
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public CardDeleteResponse deleteStolenCard(@PathVariable String number) {
        return CardDeleteResponse.fromEntity(cardService.delete(number));
    }

    @GetMapping("/api/antifraud/stolencard")
    public List<Card> getStolenCardList() {
        return cardService.findAll();
    }

}
