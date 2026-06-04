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

    // devuelve los datos de una ruta: [nombre, desc, ubic, dif, tipo, longitud, id_creador]
    public Object[] obtenerRuta(int idRuta) {
        String sql = "SELECT nombre_ruta, descripcion_ruta, ubicacion, dificultad, " +
                     "tipo_actividad, longitud, id_creador FROM RUTA WHERE id_ruta = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{
                    rs.getString("nombre_ruta"),
                    rs.getString("descripcion_ruta"),
                    rs.getString("ubicacion"),
                    rs.getString("dificultad"),
                    rs.getString("tipo_actividad"),
                    rs.getString("longitud"),
                    rs.getInt("id_creador")
                };
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ruta: " + e.getMessage());
        }
        return null;
    }

    // devuelve la valoracion media de una ruta como texto, o "-" si no tiene
    public String obtenerValoracionMedia(int idRuta) {
        String sql = "SELECT ROUND(AVG(puntuacion), 1) AS media FROM VALORACION WHERE id_ruta = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getObject("media") != null) {
                return String.valueOf(rs.getDouble("media"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener valoracion: " + e.getMessage());
        }
        return "-";
    }

    // crea una nueva ruta
    public boolean crearRuta(String nombre, String desc, String ubic, String dif,
                              String tipo, double longitud, int idCreador) {
        String sql = "INSERT INTO RUTA (nombre_ruta, descripcion_ruta, ubicacion, dificultad, " +
                     "tipo_actividad, longitud, id_creador) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, desc);
            ps.setString(3, ubic);
            ps.setString(4, dif);
            ps.setString(5, tipo);
            ps.setDouble(6, longitud);
            ps.setInt(7, idCreador);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear ruta: " + e.getMessage());
            return false;
        }
    }

    // edita una ruta existente
    public boolean editarRuta(int idRuta, String nombre, String desc, String ubic,
                               String dif, String tipo, double longitud) {
        String sql = "UPDATE RUTA SET nombre_ruta=?, descripcion_ruta=?, ubicacion=?, " +
                     "dificultad=?, tipo_actividad=?, longitud=? WHERE id_ruta=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, desc);
            ps.setString(3, ubic);
            ps.setString(4, dif);
            ps.setString(5, tipo);
            ps.setDouble(6, longitud);
            ps.setInt(7, idRuta);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al editar ruta: " + e.getMessage());
            return false;
        }
    }

    // elimina una ruta por id
    public boolean borrarRuta(int idRuta) {
        String sql = "DELETE FROM RUTA WHERE id_ruta = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idRuta);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al borrar ruta: " + e.getMessage());
            return false;
        }
    }

    // carga los comentarios de una ruta para mostrarlos en la lista
    public javax.swing.DefaultListModel<String> cargarComentarios(int idRuta) {
        javax.swing.DefaultListModel<String> listModel = new javax.swing.DefaultListModel<>();
        String sql = "SELECT u.nombre_usuario, c.fecha_comentario, c.contenido " +
                     "FROM COMENTARIO c JOIN USUARIO u ON c.id_usuario = u.id_usuario " +
                     "WHERE c.id_ruta = ? ORDER BY c.fecha_comentario DESC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String entrada = rs.getString("nombre_usuario") + " - " +
                                 rs.getString("fecha_comentario").substring(0, 10) + ": " +
                                 rs.getString("contenido");
                listModel.addElement(entrada);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar comentarios: " + e.getMessage());
        }
        return listModel;
    }

    // inserta un comentario en la BD
    public boolean insertarComentario(int idUsuario, int idRuta, String contenido) {
        String sql = "INSERT INTO COMENTARIO (id_usuario, id_ruta, contenido) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRuta);
            ps.setString(3, contenido);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar comentario: " + e.getMessage());
            return false;
        }
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
