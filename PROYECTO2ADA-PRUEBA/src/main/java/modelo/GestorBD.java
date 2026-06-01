package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestorBD {

    private final String URL      = "jdbc:mysql://localhost:3306/mvcADA";
    private final String USER     = "root";
    private final String PASSWORD = "1234";

    private Connection con;

    public GestorBD() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión OK");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }
}
