package pe.bazan.luis.attendance.backend.singleton;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class DatabaseConnection {
    private static DatabaseConnection instance;

    private Connection connection;
    private Date creationTime = new Date();

    private DatabaseConnection() throws SQLException {
        try {
            InitialContext context = new InitialContext();
            Context envContext = (Context) context.lookup("java:comp/env");
            DataSource source = (DataSource) envContext.lookup("jdbc-mysql-dwi");
            connection = source.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstancia() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConexion().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConexion() {
        return connection;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
