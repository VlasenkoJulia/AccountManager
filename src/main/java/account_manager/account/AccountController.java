package account_manager.account;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountRepository accountRepository;
    private final Gson gson;

    @Autowired
    public AccountController(AccountRepository accountRepository, Gson gson) {
        this.accountRepository = accountRepository;
        this.gson = gson;
    }

    @GetMapping("/get-by-client")
    public ModelAndView getByClientId(@RequestParam("clientId") Integer id) {
        List<Account> accounts = accountRepository.getByClientId(id);
        return new ModelAndView("accountsByClient", "accounts", accounts);
    }

    @GetMapping
    @ResponseBody
    public String getAccountById(@RequestParam("accountId") Integer id) {
        Account account = accountRepository.getById(id);
        return gson.toJson(account);
    }

    @PostMapping
    @ResponseBody
    public String createAccount(@RequestBody String body) {
        Account account = gson.fromJson(body, Account.class);
        if (account.getId() != null) {
            throw new IllegalStateException("Can not provide insert operation with passed account");
        }
        Account openAccount = accountRepository.create(account);
        return "Created account #" + openAccount.getId();
    }

    @PutMapping
    @ResponseBody
    public String updateAccount(@RequestBody String body) {
        Account account = gson.fromJson(body, Account.class);
        if (account.getId() == null) {
            throw new IllegalStateException("Can not provide update operation with passed account");
        }
        accountRepository.update(account);
        return "Account updated successfully";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteAccount(@RequestParam("accountId") Integer id) {
        accountRepository.deleteById(id);
        return "Deleted account #" + id;
    }
}