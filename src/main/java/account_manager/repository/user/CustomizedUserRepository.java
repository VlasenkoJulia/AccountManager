package account_manager.repository.user;

public interface CustomizedUserRepository<T> {
    void update(T entity);
}
