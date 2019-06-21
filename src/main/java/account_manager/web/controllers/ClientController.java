package account_manager.web.controllers;


import account_manager.client.Client;
import account_manager.client.ClientService;
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
    public Client getClientById(@RequestParam Integer clientId) {
        return clientService.getById(clientId);
    }

    @PostMapping
    @ResponseBody
    public String createClient(@RequestBody Client client){
        return clientService.create(client);
    }

    @PutMapping
    @ResponseBody
    public String updateClient(@RequestBody Client client){
        return clientService.update(client);
    }

    @DeleteMapping
    @ResponseBody
    public String deleteClient(@RequestParam Integer clientId) {
        return clientService.deleteById(clientId);
    }
}
