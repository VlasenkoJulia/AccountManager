package account_manager.web.controller;

import account_manager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdminController {
    private final ClientService clientService;

    @Autowired
    public AdminController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/indexing")
    public void index() {
        clientService.index();
    }
}
