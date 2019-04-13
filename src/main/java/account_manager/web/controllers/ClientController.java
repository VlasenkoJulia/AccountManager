package account_manager.web.controllers;


import account_manager.client.Client;
import account_manager.client.ClientRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientRepository clientRepository;
    private final Gson gson;

    @Autowired
    public ClientController(ClientRepository clientRepository, Gson gson) {
        this.clientRepository = clientRepository;
        this.gson = gson;
    }

    @GetMapping
    public String getClientById(@RequestParam("clientId") Integer id) throws InputParameterValidationException {
        Client client = clientRepository.getById(id);
        if (client == null) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        return gson.toJson(client);
    }

    @PostMapping
    public String createClient(@RequestBody String body) throws InputParameterValidationException {
        Client client = gson.fromJson(body, Client.class);
        if (client.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed client");
        }
        Client createdClient = clientRepository.create(client);
        return "Created client #" + createdClient.getId();
    }

    @PutMapping
    public String updateClient(@RequestBody String body) throws InputParameterValidationException {
        Client client = gson.fromJson(body, Client.class);

        if (client.getId() == null) {
            throw new InputParameterValidationException("Can not provide update operation with passed client");
        }
        clientRepository.update(client);
        return "Client updated successfully";
    }

    @DeleteMapping
    public String deleteClient(@RequestParam("clientId") Integer id) throws InputParameterValidationException {
        clientRepository.deleteById(id);
        return "Deleted client #" + id;
    }
}
