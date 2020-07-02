package account_manager.repository.card;

import account_manager.repository.AbstractTestRepository;
import account_manager.repository.account.AccountEntity;
import account_manager.repository.account.AccountRepository;
import account_manager.repository.account.AccountType;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class CardEntityRepositoryTest extends AbstractTestRepository {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void create() {
        CardEntity cardEntity = new CardEntity(null, "1111");
        cardRepository.save(cardEntity);
        assertEquals(1, cardRepository.count());
    }

    @Test
    public void findById() {
        CardEntity cardEntity = new CardEntity(null, "1111");
        cardRepository.save(cardEntity);
        Optional<CardEntity> foundCard = cardRepository.findById(cardEntity.getId());
        assertThat(foundCard).isPresent();
        assertEquals(cardEntity, foundCard.get());
    }

    @Test
    public void deleteById() {
        CardEntity cardEntity = new CardEntity(null, "1111");
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCards(new HashSet<>(Set.of(cardEntity)));
        cardEntity.setAccounts(new ArrayList<>(List.of(accountEntity)));
        cardRepository.save(cardEntity);
        cardRepository.deleteById(cardEntity.getId());
        assertEquals(0, cardRepository.count());
    }

    @Test(expected = InputParameterValidationException.class)
    public void deleteInvalidCard_ShouldThrowException() {
        CardEntity cardEntity = new CardEntity(null, "1111");
        cardRepository.save(cardEntity);
        cardRepository.deleteById(cardEntity.getId() + 1);
    }

    @Test
    public void findCardsByAccountId() {
        CardEntity cardEntity = new CardEntity(null, "1111");
        AccountEntity accountEntity = new AccountEntity(null, "111", null, AccountType.CURRENT, 1.0, Instant.ofEpochSecond(0L), null, new HashSet<>(Set.of(cardEntity)));
        AccountEntity savedAccountEntity = accountRepository.save(accountEntity);
        cardEntity.setAccounts(new ArrayList<>(List.of(savedAccountEntity)));
        cardRepository.save(cardEntity);
        Set<CardEntity> cardEntities = cardRepository.findByAccountId(savedAccountEntity.getId());
        assertThat(cardEntities).containsOnly(cardEntity);
    }
}
