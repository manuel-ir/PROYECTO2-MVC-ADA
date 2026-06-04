import controlador.ControladorLogin;
import modelo.GestorBD;
import vista.VistaLogin;

public class Main {

    public static void main(String[] args) {
        // arranca la aplicación con la vista de login
        GestorBD modelo = new GestorBD();
        VistaLogin vista = new VistaLogin();
        new ControladorLogin(vista, modelo);
        vista.setVisible(true);
    }
}
