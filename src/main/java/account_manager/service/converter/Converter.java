package account_manager.service.converter;

public interface Converter<T, R> {
    T convertTo(R dto);
    R convertFrom(T entity);
}
