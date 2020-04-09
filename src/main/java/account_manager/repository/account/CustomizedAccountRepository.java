package account_manager.repository.account;

public interface CustomizedAccountRepository<T, ID> {
    void deleteById(ID id);

    void update(T entity);
}
