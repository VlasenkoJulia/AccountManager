package account_manager.repository.card;

import java.util.Set;

public interface CustomizedCardRepository<T, ID> {
    void deleteById(ID id);

    Set<CardEntity> findByAccountId(Integer accountId);
}
