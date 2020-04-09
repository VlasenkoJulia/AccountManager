package account_manager.repository.card;

import java.util.List;

public interface CustomizedCardRepository<T, ID> {
    void deleteById(ID id);

    List<Card> findByAccountId(Integer accountId);
}
