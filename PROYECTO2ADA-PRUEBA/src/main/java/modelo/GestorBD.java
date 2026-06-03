package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
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

    // devuelve el id del usuario si las credenciales son correctas, -1 si no
    public int validarLogin(String email, String password) {
        String sql = "SELECT id_usuario FROM USUARIO WHERE email = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return -1;
    }

    // devuelve el nombre de usuario a partir de su id
    public String getNombreUsuario(int idUsuario) {
        String sql = "SELECT nombre_usuario FROM USUARIO WHERE id_usuario = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nombre_usuario");
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre: " + e.getMessage());
        }
        return "";
    }

    // carga las rutas en la tabla de la página principal
    public DefaultTableModel cargarRutasTabla() {
        String[] columnas = {"Nombre", "Ubicación", "Dificultad", "Valoración"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        String sql = "SELECT r.nombre_ruta, r.ubicacion, r.dificultad, " +
                     "ROUND(AVG(v.puntuacion), 1) AS valoracion " +
                     "FROM RUTA r LEFT JOIN VALORACION v ON r.id_ruta = v.id_ruta " +
                     "GROUP BY r.id_ruta ORDER BY r.nombre_ruta";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Object val = rs.getObject("valoracion");
                modelo.addRow(new Object[]{
                    rs.getString("nombre_ruta"),
                    rs.getString("ubicacion"),
                    rs.getString("dificultad"),
                    val != null ? val : "-"
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar rutas: " + e.getMessage());
        }
        return modelo;
    }

    // devuelve lista de [id_ruta, id_creador] en el mismo orden que cargarRutasTabla
    public List<int[]> cargarIdsRutas() {
        List<int[]> ids = new ArrayList<>();
        String sql = "SELECT id_ruta, id_creador FROM RUTA ORDER BY nombre_ruta";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ids.add(new int[]{rs.getInt("id_ruta"), rs.getInt("id_creador")});
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar ids: " + e.getMessage());
        }
        return ids;
    }

    // inserta un nuevo usuario en la BD
    public boolean registrarUsuario(String nombre, String email, String password) {
        String sql = "INSERT INTO USUARIO (nombre_usuario, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}
