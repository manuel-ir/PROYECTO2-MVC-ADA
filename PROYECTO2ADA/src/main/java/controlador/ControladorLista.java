package controlador;

import java.util.List;
import modelo.GestorBD;
import vista.VistaLista;
import vista.VistaRuta;

// Controla la pantalla de listas: muestra las listas del usuario y las rutas de cada una
public class ControladorLista {

    private VistaLista vista;
    private GestorBD modelo;
    private int idUsuario;
    private List<Integer> idsListas;       // ids en el mismo orden que la lista visual
    private List<Integer> idsRutasEnLista; // ids de las rutas de la lista seleccionada
    private int idListaSeleccionada = -1;

    public ControladorLista(VistaLista vista, GestorBD modelo, int idUsuario) {
        this.vista     = vista;
        this.modelo    = modelo;
        this.idUsuario = idUsuario;

        cargarListas();

        // al seleccionar una lista cargamos sus rutas en el panel derecho
        vista.getListaListas().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int idx = vista.getListaListas().getSelectedIndex();
                if (idx >= 0) {
                    idListaSeleccionada = idsListas.get(idx);
                    cargarRutasEnLista(idListaSeleccionada);
                }
            }
        });

        vista.getBtnNuevaLista().addActionListener(e -> nuevaLista());
        vista.getBtnEliminarLista().addActionListener(e -> eliminarLista());
        vista.getBtnQuitarRuta().addActionListener(e -> quitarRuta());
        vista.getBtnVerRuta().addActionListener(e -> verRuta());
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }

    // carga o recarga la lista de nombres e ids del usuario
    private void cargarListas() {
        vista.setModeloListas(modelo.cargarListasModel(idUsuario));
        idsListas = modelo.cargarIdsListas(idUsuario);
        vista.setModeloRutas(new javax.swing.DefaultListModel<>());
        idListaSeleccionada = -1;
    }

    private void cargarRutasEnLista(int idLista) {
        vista.setModeloRutas(modelo.cargarRutasEnLista(idLista));
        idsRutasEnLista = modelo.cargarIdsRutasEnLista(idLista);
    }

    private void nuevaLista() {
        String nombre = javax.swing.JOptionPane.showInputDialog(vista, "Nombre de la lista:");
        if (nombre == null || nombre.trim().isEmpty()) return;
        boolean ok = modelo.crearLista(nombre.trim(), idUsuario);
        if (ok) cargarListas();
        else vista.mostrarMensaje("Ya tienes una lista con ese nombre."); // la BD tiene un trigger para esto
    }

    private void eliminarLista() {
        if (idListaSeleccionada < 0) {
            vista.mostrarMensaje("Selecciona una lista primero.");
            return;
        }
        int confirm = javax.swing.JOptionPane.showConfirmDialog(vista,
            "Seguro que quieres eliminar esta lista?", "Confirmar",
            javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            modelo.eliminarLista(idListaSeleccionada);
            cargarListas();
        }
    }

    // abre el detalle de la ruta seleccionada en el panel derecho
    private void verRuta() {
        int idx = vista.getTablaRutaLista().getSelectedIndex();
        if (idx < 0 || idsRutasEnLista == null) {
            vista.mostrarMensaje("Selecciona una ruta primero.");
            return;
        }
        VistaRuta vistaRuta = new VistaRuta();
        new ControladorRuta(vistaRuta, modelo, idsRutasEnLista.get(idx), idUsuario);
        vistaRuta.setVisible(true);
    }

    private void quitarRuta() {
        int idx = vista.getTablaRutaLista().getSelectedIndex();
        if (idx < 0 || idListaSeleccionada < 0) {
            vista.mostrarMensaje("Selecciona una ruta primero.");
            return;
        }
        modelo.quitarRutaDeLista(idListaSeleccionada, idsRutasEnLista.get(idx));
        cargarRutasEnLista(idListaSeleccionada); // recargamos el panel derecho
    }
}
