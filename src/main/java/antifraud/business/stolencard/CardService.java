package antifraud.business.stolencard;


import antifraud.business.stolencard.dto.CardRequest;
import antifraud.exception.EntityExistsException;
import antifraud.exception.NotFoundException;
import antifraud.exception.WrongFormatException;
import antifraud.persistence.StolenCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    StolenCardRepository cardRepository;

    public CardService(StolenCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card findByNumber(String number) throws WrongFormatException, NotFoundException {
        if (!isValidLuhn(number)) {
            throw new WrongFormatException();
        }
        return cardRepository.findByNumber(number).orElseThrow(NotFoundException::new);
    }

    public boolean existsByNumber(String number) {
        if (!isValidLuhn(number)) {
            throw new WrongFormatException();
        }
        return cardRepository.findByNumber(number).isPresent();
    }

    public Card delete(String number) {
        Card card = findByNumber(number);
        cardRepository.delete(card);
        return card;
    }

    public List<Card> findAll() {
        return (List<Card>) cardRepository.findAll();
    }

    public Card save(CardRequest cardRequest) throws EntityExistsException, WrongFormatException{

        Card card = new Card();

        if (cardRepository.findByNumber(cardRequest.getNumber()).isPresent()) {
            throw new EntityExistsException();
        }

        if (!isValidLuhn(cardRequest.getNumber())) {
            throw new WrongFormatException();
        }

        card.setNumber(cardRequest.getNumber());
        return cardRepository.save(card);

    }

    public static boolean isValidLuhn(String value) {
        int sum = Character.getNumericValue(value.charAt(value.length() - 1));
        int parity = value.length() % 2;
        for (int i = value.length() - 2; i >= 0; i--) {
            int summand = Character.getNumericValue(value.charAt(i));
            if (i % 2 == parity) {
                int product = summand * 2;
                summand = (product > 9) ? (product - 9) : product;
            }
            sum += summand;
        }
        return (sum % 10) == 0;
    }
}
