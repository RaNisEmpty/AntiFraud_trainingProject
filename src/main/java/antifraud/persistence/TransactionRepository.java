package antifraud.persistence;

import antifraud.business.transaction.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByDateGreaterThanEqualAndDateLessThanEqual(LocalDateTime first, LocalDateTime second);

    List<Transaction> findAllByNumber(String number);
}
