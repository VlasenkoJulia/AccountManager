package account_manager.web.controller;


import account_manager.repository.entity.Account;
import account_manager.service.AccountService;
import account_manager.repository.entity.Client;
import account_manager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final ClientService clientService;

    @Autowired
    public AccountController(AccountService accountService, ClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @GetMapping("/get-by-client")
    public ModelAndView getByClientId(@RequestParam Integer clientId) {
        Client client = clientService.getById(clientId);
        ModelAndView modelAndView = new ModelAndView("accountsByClient");
        modelAndView.addObject("client", client);
        modelAndView.addObject("accounts", accountService.getByClientId(clientId));
        return modelAndView;
    }

    @GetMapping
    @ResponseBody
    public Account getAccountById(@RequestParam Integer accountId) {
        return accountService.getById(accountId);
    }

    @PostMapping
    @ResponseBody
    public String createAccount(@RequestBody Account account) {
        return accountService.create(account);
    }

    @PutMapping
    @ResponseBody
    public String updateAccount(@RequestBody Account account) {
        return accountService.update(account);
    }

    @DeleteMapping
    @ResponseBody
    public String deleteAccount(@RequestParam Integer accountId) {
        return accountService.deleteById(accountId);
    }
}
