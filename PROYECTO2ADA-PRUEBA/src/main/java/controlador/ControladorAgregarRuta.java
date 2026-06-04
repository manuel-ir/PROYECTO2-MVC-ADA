package controlador;

import modelo.GestorBD;
import vista.VistaFormularioRuta;

public class ControladorAgregarRuta {

    private VistaFormularioRuta vista;
    private GestorBD modelo;
    private int idUsuario;
    private int idRuta; // 0 si es nueva ruta, >0 si es edicion

    public ControladorAgregarRuta(VistaFormularioRuta vista, GestorBD modelo, int idUsuario, int idRuta) {
        this.vista     = vista;
        this.modelo    = modelo;
        this.idUsuario = idUsuario;
        this.idRuta    = idRuta;

        if (idRuta > 0) {
            vista.setTituloFormulario("Editar ruta");
            cargarDatosExistentes();
        } else {
            vista.setTituloFormulario("Nueva ruta");
        }

        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    private void cargarDatosExistentes() {
        Object[] ruta = modelo.obtenerRuta(idRuta);
        if (ruta == null) return;
        vista.cargarDatos(
            (String) ruta[0],
            ruta[2] != null ? (String) ruta[2] : "",
            ruta[3] != null ? (String) ruta[3] : "Facil",
            ruta[4] != null ? (String) ruta[4] : "Senderismo",
            ruta[5] != null ? ruta[5].toString() : "",
            ruta[1] != null ? (String) ruta[1] : ""
        );
    }

    private void guardar() {
        String nombre = vista.getNombreRuta().trim();
        String ubic   = vista.getUbicacion().trim();
        String dif    = vista.getDificultad();
        String tipo   = vista.getTipo();
        String lonStr = vista.getLongitud().trim();
        String desc   = vista.getDescripcion().trim();

        if (nombre.isEmpty() || ubic.isEmpty() || lonStr.isEmpty()) {
            vista.mostrarMensaje("Nombre, ubicacion y longitud son obligatorios.");
            return;
        }

        double longitud;
        try {
            longitud = Double.parseDouble(lonStr);
            if (longitud <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("La longitud debe ser un numero positivo.");
            return;
        }

        boolean ok;
        if (idRuta == 0) {
            ok = modelo.crearRuta(nombre, desc, ubic, dif, tipo, longitud, idUsuario);
        } else {
            ok = modelo.editarRuta(idRuta, nombre, desc, ubic, dif, tipo, longitud);
        }

        if (ok) {
            vista.dispose();
        } else {
            vista.mostrarMensaje("Error al guardar la ruta.");
        }
    }
}
