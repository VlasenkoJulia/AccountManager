package account_manager.repository.client;

public interface CustomizedClientRepository<T, ID> {
    void deleteById(ID id);

    void update(T entity);
}
