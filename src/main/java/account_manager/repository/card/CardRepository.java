package account_manager.repository.card;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Integer>,
        CustomizedCardRepository<CardEntity, Integer> {

}
