package controlador;

import modelo.GestorBD;
import vista.VistaRuta;

public class ControladorRuta {

    private VistaRuta vista;
    private GestorBD modelo;
    private int idRuta;
    private int idUsuario;

    public ControladorRuta(VistaRuta vista, GestorBD modelo, int idRuta, int idUsuario) {
        this.vista     = vista;
        this.modelo    = modelo;
        this.idRuta    = idRuta;
        this.idUsuario = idUsuario;

        cargarDatos();
        cargarComentarios();

        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
        vista.getBtnComentar().addActionListener(e -> comentar());
        vista.getBtnValorar().addActionListener(e -> valorar());
        vista.getBtnGuardarEnLista().addActionListener(e -> guardarEnLista());
    }

    private void cargarDatos() {
        Object[] ruta = modelo.obtenerRuta(idRuta);
        if (ruta == null) return;

        vista.setNombreRuta((String) ruta[0]);
        vista.setDescripcion(ruta[1] != null ? (String) ruta[1] : "");
        vista.setUbicacion(ruta[2] != null ? (String) ruta[2] : "");
        vista.setDificultad(ruta[3] != null ? (String) ruta[3] : "");
        vista.setLongitud(ruta[5] != null ? ruta[5] + " km" : "");
        vista.setValoracion(modelo.obtenerValoracionMedia(idRuta));
        vista.setAutor(modelo.getNombreUsuario((int) ruta[6]));
    }

    private void cargarComentarios() {
        vista.setComentarios(modelo.cargarComentarios(idRuta));
    }

    private void comentar() {
        String texto = vista.getNuevoComentario().trim();
        if (texto.isEmpty()) {
            vista.mostrarMensaje("Escribe un comentario antes de enviar.");
            return;
        }
        modelo.insertarComentario(idUsuario, idRuta, texto);
        vista.limpiarComentario();
        cargarComentarios();
    }

    private void valorar() {
        // se implementa en funcionalidadValoraciones
    }

    private void guardarEnLista() {
        // se implementa en funcionalidadListas
    }
}
