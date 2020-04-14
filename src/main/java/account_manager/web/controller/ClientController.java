package account_manager.web.controller;


import account_manager.service.ClientService;
import account_manager.service.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @ResponseBody
    public ClientDto getClientById(@RequestParam Integer clientId) {
        return clientService.getById(clientId);
    }

    @PostMapping
    @ResponseBody
    public String createClient(@RequestBody ClientDto client) {
        return clientService.create(client);
    }

    @PutMapping
    @ResponseBody
    public String updateClient(@RequestBody ClientDto client) {
        return clientService.update(client);
    }

    @DeleteMapping
    @ResponseBody
    public String deleteClient(@RequestParam Integer clientId) {
        return clientService.deleteById(clientId);
    }
}
