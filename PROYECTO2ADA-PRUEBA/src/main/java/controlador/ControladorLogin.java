package controlador;

import modelo.GestorBD;
import vista.VistaLogin;
import vista.VistaRegistro;
import vista.VistaPaginaPrincipal;

public class ControladorLogin {

    private VistaLogin vista;
    private GestorBD modelo;

    public ControladorLogin(VistaLogin vista, GestorBD modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // listener botón entrar
        vista.getBtnLogin().addActionListener(e -> login());

        // listener botón ir a registro
        vista.getBtnRegistro().addActionListener(e -> irARegistro());
    }

    private void login() {
        String email    = vista.getEmail().trim();
        String password = vista.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje("Rellena todos los campos.");
            return;
        }

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

    private void irARegistro() {
        VistaRegistro vistaRegistro = new VistaRegistro();
        new ControladorRegistro(vistaRegistro, modelo);
        vistaRegistro.setVisible(true);
    }
}
