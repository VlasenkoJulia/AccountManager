package account_manager.client;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("id");
        String lastName = rs.getString("last_name");
        String firstName = rs.getString("first_name");
        return new Client(id, lastName, firstName);
    }
}
