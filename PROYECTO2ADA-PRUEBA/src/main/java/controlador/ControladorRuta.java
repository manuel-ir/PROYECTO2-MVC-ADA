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
        Object[] ruta = modelo.obtenerRuta(idRuta);
        if (ruta != null && (int) ruta[6] == idUsuario) {
            vista.mostrarMensaje("No puedes valorar tu propia ruta.");
            return;
        }

        String[] opciones = {"1", "2", "3", "4", "5"};
        String puntuacionStr = (String) javax.swing.JOptionPane.showInputDialog(
            vista, "Puntuacion (1-5):", "Valorar ruta",
            javax.swing.JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[4]);
        if (puntuacionStr == null) return;

        String comentario = javax.swing.JOptionPane.showInputDialog(vista, "Comentario (opcional):");
        if (comentario == null) return;

        boolean ok = modelo.insertarValoracion(idUsuario, idRuta, Integer.parseInt(puntuacionStr), comentario);
        if (ok) {
            vista.setValoracion(modelo.obtenerValoracionMedia(idRuta));
            vista.mostrarMensaje("Valoracion guardada.");
        } else {
            vista.mostrarMensaje("Ya has valorado esta ruta o ha ocurrido un error.");
        }
    }

    private void guardarEnLista() {
        java.util.List<Integer> ids = modelo.cargarIdsListas(idUsuario);
        javax.swing.DefaultListModel<String> nombresModel = modelo.cargarListasModel(idUsuario);
        if (ids.isEmpty()) {
            vista.mostrarMensaje("No tienes ninguna lista. Crea una desde Mis Listas.");
            return;
        }
        String[] opciones = new String[nombresModel.size()];
        for (int i = 0; i < nombresModel.size(); i++) opciones[i] = nombresModel.get(i);

        String elegida = (String) javax.swing.JOptionPane.showInputDialog(
            vista, "Selecciona una lista:", "Guardar en lista",
            javax.swing.JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (elegida != null) {
            int idLista = ids.get(java.util.Arrays.asList(opciones).indexOf(elegida));
            boolean ok = modelo.agregarRutaALista(idLista, idRuta);
            vista.mostrarMensaje(ok ? "Ruta guardada en la lista." : "La ruta ya estaba en esa lista.");
        }
    }
}
