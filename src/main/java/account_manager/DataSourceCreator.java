package account_manager;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DataSourceCreator {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank_db";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static DataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASS);
        return dataSource;
    }
}
