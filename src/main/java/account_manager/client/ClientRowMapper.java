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
        Integer id = (Integer) rs.getObject("id");
        String lastName = rs.getString("last_name");
        String firstName = rs.getString("first_name");
        return new Client(id, lastName, firstName);
    }
}
