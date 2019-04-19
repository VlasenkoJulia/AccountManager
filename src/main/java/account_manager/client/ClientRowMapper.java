package account_manager.client;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClientRowMapper implements RowMapper<Client> {
    @Nullable
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
        client.setId((Integer) rs.getObject("id"));
        client.setLastName(rs.getString("last_name"));
        client.setFirstName(rs.getString("first_name"));
        return client;
    }
}
