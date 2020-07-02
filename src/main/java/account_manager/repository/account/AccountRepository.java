package account_manager.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer>,
        CustomizedAccountRepository<AccountEntity, Integer> {

    List<AccountEntity> findAllByClientId(Integer clientId);
}
