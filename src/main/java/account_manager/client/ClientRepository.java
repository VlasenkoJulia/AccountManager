package account_manager.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ClientRowMapper clientRowMapper;
    private final SimpleJdbcInsert simpleJdbcInsert;
    @Autowired
    public ClientRepository(JdbcTemplate jdbcTemplate, ClientRowMapper clientRowMapper, SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.clientRowMapper = clientRowMapper;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    Client create(Client client) {
        SimpleJdbcInsert insert = simpleJdbcInsert
                .withTableName("client")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("last_name", client.getLastName());
        parameters.put("first_name", client.getFirstName());
        int createdClientId = insert.executeAndReturnKey(parameters).intValue();
        return getById(createdClientId);
    }

    Client getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM client WHERE id = ?", clientRowMapper, id);
    }

    void deleteById(int id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM client WHERE id = ?", id);
        if (rowsAffected < 1) {
            throw new RuntimeException("Client with passed ID do not exist");
        }
    }

    Client update(Client client) {
        int rowsAffected = jdbcTemplate.update("UPDATE client SET last_name = ?, first_name = ? WHERE id  = ?",
                client.getLastName(), client.getFirstName(), client.getId());
        if (rowsAffected < 1) {
            throw new RuntimeException("Client with passed ID do not exist");
        }
        return getById(client.getId());
    }
}
