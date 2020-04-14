package account_manager.repository.card;

import java.util.Set;

public interface CustomizedCardRepository<T, ID> {
    void deleteById(ID id);

    Set<Card> findByAccountId(Integer accountId);
}
