package controlador;

import java.util.List;
import modelo.GestorBD;
import vista.VistaLogin;
import vista.VistaPaginaPrincipal;

public class ControladorPrincipal {

    private VistaPaginaPrincipal vista;
    private GestorBD modelo;
    private int idUsuario;
    private List<int[]> idsRutas; // [id_ruta, id_creador] por fila

    public ControladorPrincipal(VistaPaginaPrincipal vista, GestorBD modelo, int idUsuario) {
        this.vista     = vista;
        this.modelo    = modelo;
        this.idUsuario = idUsuario;

        // carga nombre y tabla al abrir
        vista.setNombreUsuario(modelo.getNombreUsuario(idUsuario));
        cargarTabla();

        // los botones de editar y borrar solo se activan al seleccionar una fila propia
        vista.getTablaRutas().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) actualizarBotones();
        });

        vista.getBtnNuevaRuta().addActionListener(e -> nuevaRuta());
        vista.getBtnVerRuta().addActionListener(e -> verRuta());
        vista.getBtnEditarRuta().addActionListener(e -> editarRuta());
        vista.getBtnBorrarRuta().addActionListener(e -> borrarRuta());
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void cargarTabla() {
        vista.getTablaRutas().setModel(modelo.cargarRutasTabla());
        idsRutas = modelo.cargarIdsRutas();
        actualizarBotones();
    }

    // activa editar y borrar solo si la fila seleccionada pertenece al usuario
    private void actualizarBotones() {
        int fila = vista.getTablaRutas().getSelectedRow();
        boolean seleccionada = fila >= 0;
        boolean esCreador    = seleccionada && idsRutas.get(fila)[1] == idUsuario;

        vista.getBtnVerRuta().setEnabled(seleccionada);
        vista.getBtnEditarRuta().setEnabled(esCreador);
        vista.getBtnBorrarRuta().setEnabled(esCreador);
    }

    private void nuevaRuta() {
    }

    private void verRuta() {
    }

    private void editarRuta() {
    }

    private void borrarRuta() {
    }

    private void cerrarSesion() {
        vista.dispose();
        VistaLogin vistaLogin = new VistaLogin();
        new ControladorLogin(vistaLogin, modelo);
        vistaLogin.setVisible(true);
    }
}
