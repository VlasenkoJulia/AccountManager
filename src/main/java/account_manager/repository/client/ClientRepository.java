package account_manager.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer>,
        CustomizedClientRepository<ClientEntity, Integer> {
}
