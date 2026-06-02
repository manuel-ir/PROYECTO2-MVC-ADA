package controlador;

import modelo.GestorBD;
import vista.VistaRegistro;

public class ControladorRegistro {

    private VistaRegistro vista;
    private GestorBD modelo;

    public ControladorRegistro(VistaRegistro vista, GestorBD modelo) {
        this.vista = vista;
        this.modelo = modelo;

        // listener del botón registrar
        vista.getBtnRegistrar().addActionListener(e -> registrar());
    }

    private void registrar() {
        String nombre  = vista.getNombre().trim();
        String email   = vista.getEmail().trim();
        String pass    = vista.getPass();
        String confirm = vista.getPassConfirm();

        // campos vacíos
        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Rellena todos los campos.");
            return;
        }

        // nombre mínimo 3 caracteres
        if (nombre.length() < 3) {
            vista.mostrarMensaje("El nombre de usuario debe tener al menos 3 caracteres.");
            return;
        }

        // formato email
        if (!email.equals(email.toLowerCase()) || !email.contains("@") || !email.endsWith("gmail.com")) {
            vista.mostrarMensaje("El email debe estar en minúsculas y acabar en gmail.com.");
            return;
        }

        // contraseñas coinciden
        if (!pass.equals(confirm)) {
            vista.mostrarMensaje("Las contraseñas no coinciden.");
            return;
        }

        // complejidad contraseña
        if (!validarPassword(pass)) {
            vista.mostrarMensaje("La contraseña debe tener al menos 10 caracteres, una mayúscula, una minúscula y un número.");
            return;
        }

        boolean ok = modelo.registrarUsuario(nombre, email, pass);
        if (ok) {
            vista.mostrarMensaje("Cuenta creada correctamente. Ya puedes iniciar sesión.");
            vista.dispose();
        } else {
            vista.mostrarMensaje("Error al crear la cuenta. El nombre o email ya existe.");
        }
    }

    // comprueba mayúscula, minúscula, número y longitud mínima
    private boolean validarPassword(String pass) {
        if (pass.length() < 10) return false;
        boolean mayuscula = false, minuscula = false, numero = false;
        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) mayuscula = true;
            if (Character.isLowerCase(c)) minuscula = true;
            if (Character.isDigit(c))     numero    = true;
        }
        return mayuscula && minuscula && numero;
    }
}
