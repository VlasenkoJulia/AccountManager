package account_manager.repository.card;

import account_manager.repository.AbstractTestRepository;
import account_manager.repository.account.Account;
import account_manager.repository.account.AccountRepository;
import account_manager.repository.account.AccountType;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class CardRepositoryTest extends AbstractTestRepository {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void create() {
        Card card = new Card(null, "1111");
        cardRepository.save(card);
        assertEquals(1, cardRepository.count());
    }

    @Test
    public void findById() {
        Card card = new Card(null, "1111");
        cardRepository.save(card);
        Optional<Card> foundCard = cardRepository.findById(card.getId());
        assertThat(foundCard).isPresent();
        assertEquals(card, foundCard.get());
    }

    @Test
    public void deleteById() {
        Card card = new Card(null, "1111");
        Account account = new Account();
        account.setCards(new HashSet<>(Set.of(card)));
        card.setAccounts(new ArrayList<>(List.of(account)));
        cardRepository.save(card);
        cardRepository.deleteById(card.getId());
        assertEquals(0, cardRepository.count());
    }

    @Test(expected = InputParameterValidationException.class)
    public void deleteInvalidCard_ShouldThrowException() {
        Card card = new Card(null, "1111");
        cardRepository.save(card);
        cardRepository.deleteById(card.getId() + 1);
    }

    @Test
    public void findCardsByAccountId() {
        Card card = new Card(null, "1111");
        Account account = new Account(null, "111", null, AccountType.CURRENT, 1.0, Instant.ofEpochSecond(0L), null, new HashSet<>(Set.of(card)));
        Account savedAccount = accountRepository.save(account);
        card.setAccounts(new ArrayList<>(List.of(savedAccount)));
        cardRepository.save(card);
        Set<Card> cards = cardRepository.findByAccountId(savedAccount.getId());
        assertThat(cards).containsOnly(card);
    }
}
