package controlador;

import modelo.GestorBD;
import vista.VistaLogin;
import vista.VistaRegistro;
import vista.VistaPaginaPrincipal;

// Controla la pantalla de login: valida credenciales y navega a las siguientes pantallas
public class ControladorLogin {

    private VistaLogin vista;
    private GestorBD modelo;

    public ControladorLogin(VistaLogin vista, GestorBD modelo) {
        this.vista = vista;
        this.modelo = modelo;

        vista.getBtnLogin().addActionListener(e -> login());
        vista.getBtnRegistro().addActionListener(e -> irARegistro());
    }

    private void login() {
        String email    = vista.getEmail().trim();
        String password = vista.getPassword();

        // comprobamos que no haya campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje("Rellena todos los campos.");
            return;
        }

        // validarLogin devuelve el id del usuario si los datos son correctos, -1 si no
        int idUsuario = modelo.validarLogin(email, password);
        if (idUsuario != -1) {
            vista.dispose();
            VistaPaginaPrincipal vistaPrincipal = new VistaPaginaPrincipal();
            new ControladorPrincipal(vistaPrincipal, modelo, idUsuario);
            vistaPrincipal.setVisible(true);
        } else {
            vista.mostrarMensaje("Email o contraseña incorrectos.");
        }
    }

    // abre la pantalla de registro sin cerrar la conexión al modelo
    private void irARegistro() {
        VistaRegistro vistaRegistro = new VistaRegistro();
        new ControladorRegistro(vistaRegistro, modelo);
        vistaRegistro.setVisible(true);
    }
}
