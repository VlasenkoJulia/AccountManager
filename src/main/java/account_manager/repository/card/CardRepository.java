package account_manager.repository.card;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer>,
        CustomizedCardRepository<Card, Integer> {

}
