package account_manager.web.controller;


import account_manager.service.ClientService;
import account_manager.service.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ClientDto getClientById(@RequestParam Integer clientId) {
        return clientService.getById(clientId);
    }

    @PostMapping
    public String createClient(@RequestBody ClientDto client) {
        return clientService.create(client);
    }

    @PutMapping
    public String updateClient(@RequestBody ClientDto client) {
        return clientService.update(client);
    }

    @DeleteMapping
    public String deleteClient(@RequestParam Integer clientId) {
        return clientService.deleteById(clientId);
    }

    @GetMapping("/search")
    public List<ClientDto> search(@RequestParam String query) {
        List<ClientDto> search = clientService.search(query);
        return search;
    }
}
