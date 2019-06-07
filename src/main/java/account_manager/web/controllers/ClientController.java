package account_manager.web.controllers;


import account_manager.client.Client;
import account_manager.client.ClientRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    @ResponseBody
    public Client getClientById(@RequestParam Integer clientId) {
        Client client = clientRepository.getById(clientId);
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        return client;
    }

    @PostMapping
    @ResponseBody
    public String createClient(@RequestBody Client client){
        if (client.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed client");
        }
        Client createdClient = clientRepository.create(client);
        return "Created client #" + createdClient.getId();
    }

    @PutMapping
    @ResponseBody
    public String updateClient(@RequestBody Client client){
        if (client.getId() == null) {
            throw new InputParameterValidationException("Can not provide update operation with passed client");
        }
        clientRepository.update(client);
        return "Client updated successfully";
    }

    @DeleteMapping
    @ResponseBody
    public String deleteClient(@RequestParam Integer clientId) {
        clientRepository.deleteById(clientId);
        return "Deleted client #" + clientId;
    }
}
