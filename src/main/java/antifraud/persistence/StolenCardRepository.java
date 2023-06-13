package antifraud.persistence;

import antifraud.business.stolencard.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StolenCardRepository extends CrudRepository<Card, Long> {
    Optional<Card> findByNumber(String number);
}
