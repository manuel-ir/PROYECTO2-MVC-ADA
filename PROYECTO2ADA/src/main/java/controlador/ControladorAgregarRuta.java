package controlador;

import modelo.GestorBD;
import vista.VistaFormularioRuta;

// Sirve tanto para crear una ruta nueva como para editar una existente (idRuta=0 es nueva)
public class ControladorAgregarRuta {

    private VistaFormularioRuta vista;
    private GestorBD modelo;
    private int idUsuario;
    private int idRuta; // 0 si es nueva ruta, >0 si es edición

    public ControladorAgregarRuta(VistaFormularioRuta vista, GestorBD modelo, int idUsuario, int idRuta) {
        this.vista     = vista;
        this.modelo    = modelo;
        this.idUsuario = idUsuario;
        this.idRuta    = idRuta;

        // si es edición cargamos los datos existentes y cambiamos el título
        if (idRuta > 0) {
            vista.setTituloFormulario("Editar ruta");
            cargarDatosExistentes();
        } else {
            vista.setTituloFormulario("Nueva ruta");
        }

        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    // rellena los campos del formulario con los datos actuales de la ruta
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

        // convertimos la longitud a double y comprobamos que sea positiva
        double longitud;
        try {
            longitud = Double.parseDouble(lonStr);
            if (longitud <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("La longitud debe ser un numero positivo.");
            return;
        }

        // según si es nueva o edición llamamos a un método diferente del modelo
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
