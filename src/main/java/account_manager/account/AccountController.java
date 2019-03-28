package account_manager.account;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountRepository accountRepository;
    private final Gson gson;

    @Autowired
    public AccountController(AccountRepository accountRepository, Gson gson) {
        this.accountRepository = accountRepository;
        this.gson = gson;
    }

    @GetMapping
    public String getAccountById(@RequestParam("accountId") Integer id) {
        Account account = accountRepository.getById(id);
        return gson.toJson(account);
    }

    @PostMapping
    public String createAccount(@RequestBody String body) {
        Account account = gson.fromJson(body, Account.class);
        if (account.getId() != null) {
            throw new IllegalStateException("Can not provide insert operation with passed account");
        }
        Account openAccount = accountRepository.create(account);
        return "Created account #" + openAccount.getId();
    }

    @PutMapping
    public String updateAccount(@RequestBody String body) {
        Account account = gson.fromJson(body, Account.class);
        if (account.getId() == null) {
            throw new IllegalStateException("Can not provide update operation with passed account");
        }
        accountRepository.update(account);
        return "Account updated successfully";
    }

    @DeleteMapping
    public String deleteAccount(@RequestParam("accountId") Integer id) {
        accountRepository.deleteById(id);
        return "Deleted account #" + id;
    }


}
