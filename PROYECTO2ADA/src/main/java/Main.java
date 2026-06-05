import controlador.ControladorLogin;
import modelo.GestorBD;
import vista.VistaLogin;

// Punto de entrada de la aplicación TrailMate
public class Main {

    public static void main(String[] args) {
        // creamos el modelo (conexión a la BD) y arrancamos con el login
        GestorBD modelo = new GestorBD();
        VistaLogin vista = new VistaLogin();
        new ControladorLogin(vista, modelo); // el controlador conecta vista y modelo
        vista.setVisible(true);
    }
}
