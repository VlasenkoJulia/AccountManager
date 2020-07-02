package account_manager.service.converter;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.card.CardEntity;
import account_manager.service.dto.CardDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardConverter implements Converter<CardEntity, CardDto> {

    @Override
    public CardEntity convertTo(CardDto dto) {
        List<AccountEntity> accountEntities = dto.getAccountIds()
                .stream()
                .map(this::createAccount)
                .collect(Collectors.toList());
        return new CardEntity(dto.getId(), dto.getNumber(), accountEntities);
    }

    private AccountEntity createAccount(Integer id) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        return accountEntity;
    }

    @Override
    public CardDto convertFrom(CardEntity entity) {
        List<Integer> accountIds = entity.getAccounts()
                .stream()
                .map(AccountEntity::getId)
                .collect(Collectors.toList());
        return new CardDto(entity.getId(), entity.getNumber(), accountIds);
    }
}
