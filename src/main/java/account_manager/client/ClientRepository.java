package account_manager.client;

import account_manager.DataSourceCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;

class ClientRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceCreator.createDataSource());

    Client create(Client client) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(DataSourceCreator.createDataSource())
                .withTableName("client")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("last_name", client.getLastName());
        parameters.put("first_name", client.getFirstName());
        int createdClientId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        return getById(createdClientId);
    }

    Client getById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM client WHERE id = ?", new ClientRowMapper(), id);
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
