package account_manager.service;

import java.util.List;

public interface SearchClient<T> {
    void bulk(List<T> documents);

    List<T> search(String searchText);

    void index(T document);

    void update(T document);

    void delete(String documentId);
}
