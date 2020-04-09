package account_manager.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer>,
        CustomizedAccountRepository<Account, Integer> {

    List<Account> findAllByClientId(Integer clientId);
}
