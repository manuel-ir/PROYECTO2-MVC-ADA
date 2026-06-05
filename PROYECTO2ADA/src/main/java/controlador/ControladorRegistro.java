package controlador;

import modelo.GestorBD;
import vista.VistaLogin;
import vista.VistaRegistro;

// Controla el registro de nuevos usuarios con validación de datos antes de insertar en la BD
public class ControladorRegistro {

    private VistaRegistro vista;
    private GestorBD modelo;

    public ControladorRegistro(VistaRegistro vista, GestorBD modelo) {
        this.vista = vista;
        this.modelo = modelo;

        vista.getBtnRegistrar().addActionListener(e -> registrar());

        // si el usuario se equivoca de pantalla puede volver al login
        vista.getBtnVolverLogin().addActionListener(e -> {
            vista.dispose();
            VistaLogin vistaLogin = new VistaLogin();
            new ControladorLogin(vistaLogin, modelo);
            vistaLogin.setVisible(true);
        });
    }

    private void registrar() {
        String nombre  = vista.getNombre().trim();
        String email   = vista.getEmail().trim();
        String pass    = vista.getPass();
        String confirm = vista.getPassConfirm();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            vista.mostrarMensaje("Rellena todos los campos.");
            return;
        }

        if (nombre.length() < 3) {
            vista.mostrarMensaje("El nombre de usuario debe tener al menos 3 caracteres.");
            return;
        }

        // el email tiene que ser de gmail y en minúsculas
        if (!email.equals(email.toLowerCase()) || !email.contains("@") || !email.endsWith("gmail.com")) {
            vista.mostrarMensaje("El email debe estar en minúsculas y acabar en gmail.com.");
            return;
        }

        if (!pass.equals(confirm)) {
            vista.mostrarMensaje("Las contraseñas no coinciden.");
            return;
        }

        // validamos la complejidad antes de enviar a la BD
        if (!validarPassword(pass)) {
            vista.mostrarMensaje("La contraseña debe tener al menos 10 caracteres, una mayúscula, una minúscula y un número.");
            return;
        }

        // registrarUsuario usa una transacción: inserta usuario y crea su lista Favoritos
        boolean ok = modelo.registrarUsuario(nombre, email, pass);
        if (ok) {
            vista.mostrarMensaje("Cuenta creada correctamente. Ya puedes iniciar sesión.");
            vista.dispose();
        } else {
            vista.mostrarMensaje("Error al crear la cuenta. El nombre o email ya existe.");
        }
    }

    // recorre la contraseña carácter a carácter buscando mayúscula, minúscula y número
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
