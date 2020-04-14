package account_manager.service.converter;

import account_manager.repository.account.Account;
import account_manager.repository.card.Card;
import account_manager.service.dto.CardDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardConverter implements Converter<Card, CardDto> {

    @Override
    public Card convertTo(CardDto dto) {
        List<Account> accounts = dto.getAccountIds()
                .stream()
                .map(this::createAccount)
                .collect(Collectors.toList());
        return new Card(dto.getId(), dto.getNumber(), accounts);
    }

    private Account createAccount(Integer id) {
        Account account = new Account();
        account.setId(id);
        return account;
    }

    @Override
    public CardDto convertFrom(Card entity) {
        List<Integer> accountIds = entity.getAccounts()
                .stream()
                .map(Account::getId)
                .collect(Collectors.toList());
        return new CardDto(entity.getId(), entity.getNumber(), accountIds);
    }
}
