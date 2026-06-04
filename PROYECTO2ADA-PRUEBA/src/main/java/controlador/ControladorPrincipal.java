package controlador;

import java.util.List;
import modelo.GestorBD;
import vista.VistaFormularioRuta;
import vista.VistaLista;
import vista.VistaLogin;
import vista.VistaPaginaPrincipal;
import vista.VistaRuta;

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
        vista.getBtnMisListas().addActionListener(e -> abrirMisListas());
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
        VistaFormularioRuta vistaForm = new VistaFormularioRuta();
        new ControladorAgregarRuta(vistaForm, modelo, idUsuario, 0);
        vistaForm.setVisible(true);
        vistaForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) { cargarTabla(); }
        });
    }

    private void verRuta() {
        int fila = vista.getTablaRutas().getSelectedRow();
        if (fila < 0) return;
        int idRuta = idsRutas.get(fila)[0];
        VistaRuta vistaRuta = new VistaRuta();
        new ControladorRuta(vistaRuta, modelo, idRuta, idUsuario);
        vistaRuta.setVisible(true);
    }

    private void editarRuta() {
        int fila = vista.getTablaRutas().getSelectedRow();
        if (fila < 0) return;
        int idRuta = idsRutas.get(fila)[0];
        VistaFormularioRuta vistaForm = new VistaFormularioRuta();
        new ControladorAgregarRuta(vistaForm, modelo, idUsuario, idRuta);
        vistaForm.setVisible(true);
        vistaForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) { cargarTabla(); }
        });
    }

    private void borrarRuta() {
        int fila = vista.getTablaRutas().getSelectedRow();
        if (fila < 0) return;
        int confirm = javax.swing.JOptionPane.showConfirmDialog(vista,
            "Seguro que quieres borrar esta ruta?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            modelo.borrarRuta(idsRutas.get(fila)[0]);
            cargarTabla();
        }
    }

    private void abrirMisListas() {
        VistaLista vistaLista = new VistaLista();
        new ControladorLista(vistaLista, modelo, idUsuario);
        vistaLista.setVisible(true);
    }

    private void cerrarSesion() {
        vista.dispose();
        VistaLogin vistaLogin = new VistaLogin();
        new ControladorLogin(vistaLogin, modelo);
        vistaLogin.setVisible(true);
    }
}
