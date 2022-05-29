package db;

import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.ConnectionPoolDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DBConnection {
    //Так зроблено через те, що при деплої не бачить файли з пропертями
    private static final String SERVER_NAME = "localhost";
    public static final String DATABASE_NAME = "java_lab2_var17";
    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/java_lab2_var17";
    public static final String DATABASE_USERNAME = "postgres";
    public static final String DATABASE_PASSWORD = "1234";

    private static ConnectionPoolDataSource poolDataSource;

    private DBConnection() {
    }

    static {
        try {
            reconnect();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void reconnect() throws IOException, ClassNotFoundException {
        PGConnectionPoolDataSource source = new PGConnectionPoolDataSource();
        source.setServerName(SERVER_NAME);
        source.setDatabaseName(DATABASE_NAME);
        source.setUser(DATABASE_USERNAME);
        source.setPassword(DATABASE_PASSWORD);

        poolDataSource = source;
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = poolDataSource.getPooledConnection().getConnection();
        try {
            if (Objects.isNull(connection)) throw new NullPointerException();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
