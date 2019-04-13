package account_manager.client;

import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ClientRowMapper clientRowMapper;

    @Autowired
    public ClientRepository(JdbcTemplate jdbcTemplate, ClientRowMapper clientRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.clientRowMapper = clientRowMapper;
    }

    public Client create(Client client){
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("client")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("last_name", client.getLastName());
        parameters.put("first_name", client.getFirstName());
        int createdClientId = insert.executeAndReturnKey(parameters).intValue();
        return getById(createdClientId);
    }

    public Client getById(int id) {
        List<Client> clients = jdbcTemplate.query("SELECT * FROM client WHERE id = ?", clientRowMapper, id);
        return DataAccessUtils.singleResult(clients);
    }

    public void deleteById(int id) throws InputParameterValidationException {
        int rowsAffected = jdbcTemplate.update("DELETE FROM client WHERE id = ?", id);
        if (rowsAffected < 1) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
    }

    public Client update(Client client) throws InputParameterValidationException {
        int rowsAffected = jdbcTemplate.update("UPDATE client SET last_name = ?, first_name = ? WHERE id  = ?",
                client.getLastName(), client.getFirstName(), client.getId());
        if (rowsAffected < 1) {
            throw new InputParameterValidationException("Client with passed ID do not exist");
        }
        return getById(client.getId());
    }
}
