package account_manager.web.controllers;


import account_manager.account.Account;
import account_manager.account.AccountRepository;
import account_manager.client.Client;
import account_manager.client.ClientRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
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
    private final ClientRepository clientRepository;
    private final Gson gson;

    @Autowired
    public AccountController(AccountRepository accountRepository, Gson gson, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.gson = gson;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/get-by-client")
    public ModelAndView getByClientId(@RequestParam Integer clientId) throws InputParameterValidationException {
        Client client = clientRepository.getById(clientId);
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        List<Account> accounts = accountRepository.getByClientId(clientId);
        ModelAndView modelAndView = new ModelAndView("accountsByClient");
        modelAndView.addObject("client", client);
        modelAndView.addObject("accounts", accounts);
        return modelAndView;
    }

    @GetMapping
    @ResponseBody
    public String getAccountById(@RequestParam Integer accountId) throws InputParameterValidationException {
        Account account = accountRepository.getById(accountId);
        if (account == null) {
            throw new InputParameterValidationException("Account with passed ID do not exist");
        }
        return gson.toJson(account);
    }

    @PostMapping
    @ResponseBody
    public String createAccount(@RequestBody String body) throws InputParameterValidationException {
        Account account = gson.fromJson(body, Account.class);
        if (account.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed account");
        }
        if (account.getType() == null || account.getCurrencyCode().equals("")) {
            throw new InputParameterValidationException("Missed account type and/or currency");
        }
        Account openAccount = accountRepository.create(account);
        return "Created account #" + openAccount.getId();
    }

    @PutMapping
    @ResponseBody
    public String updateAccount(@RequestBody String body) throws InputParameterValidationException {
        Account account = gson.fromJson(body, Account.class);
        if (account.getId() == null) {
            throw new InputParameterValidationException("Can not provide update operation with passed account");
        }

        accountRepository.update(account);
        return "Account updated successfully";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteAccount(@RequestParam Integer accountId) throws InputParameterValidationException {
        accountRepository.deleteById(accountId);
        return "Deleted account #" + accountId;
    }
}