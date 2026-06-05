package vista;

public class VistaFormularioRuta extends javax.swing.JFrame {

    public VistaFormularioRuta() {
        initComponents();
        aplicarEstilo();
    }

    private void aplicarEstilo() {
        java.awt.Color fondoOscuro = new java.awt.Color(45, 45, 45);
        java.awt.Color fondoCampo  = new java.awt.Color(64, 64, 64);
        java.awt.Color textoBlanco = new java.awt.Color(255, 255, 255);
        java.awt.Color textoGris   = new java.awt.Color(170, 170, 170);
        javax.swing.border.Border borde = javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8);

        getContentPane().setBackground(fondoOscuro);

        jLabel1.setForeground(textoGris);
        jLabel2.setForeground(textoGris);
        lblUbicacion.setForeground(textoGris);
        lblDificultad.setForeground(textoGris);
        lblKm.setForeground(textoGris);
        lblDescripcion.setForeground(textoGris);

        txtNombreRuta.setBackground(fondoCampo); txtNombreRuta.setForeground(textoBlanco); txtNombreRuta.setCaretColor(textoBlanco); txtNombreRuta.setBorder(borde);
        txtUbicacion.setBackground(fondoCampo);  txtUbicacion.setForeground(textoBlanco);  txtUbicacion.setCaretColor(textoBlanco);  txtUbicacion.setBorder(borde);
        txtLongitud.setBackground(fondoCampo);   txtLongitud.setForeground(textoBlanco);   txtLongitud.setCaretColor(textoBlanco);   txtLongitud.setBorder(borde);

        comboDificultad.setBackground(fondoCampo); comboDificultad.setForeground(textoBlanco);
        comboTipo.setBackground(fondoCampo);       comboTipo.setForeground(textoBlanco);

        jTextArea1.setBackground(fondoCampo);
        jTextArea1.setForeground(textoBlanco);
        jTextArea1.setCaretColor(textoBlanco);

        btnCancelar.setBackground(fondoCampo);
        btnCancelar.setForeground(textoGris);
        btnCancelar.setFocusPainted(false);

        btnGuardar.setBackground(new java.awt.Color(45, 80, 22));
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setFocusPainted(false);
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        lblTituloFormuulario = new javax.swing.JLabel();
        txtNombreRuta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JTextField();
        comboDificultad = new javax.swing.JComboBox<>();
        comboTipo = new javax.swing.JComboBox<>();
        lblUbicacion = new javax.swing.JLabel();
        lblKm = new javax.swing.JLabel();
        lblDificultad = new javax.swing.JLabel();
        txtLongitud = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        lblDescripcion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeader.setBackground(new java.awt.Color(45, 80, 22));

        lblTituloFormuulario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTituloFormuulario.setForeground(new java.awt.Color(255, 255, 255));
        lblTituloFormuulario.setText("Nueva ruta");

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblTituloFormuulario)
                .addContainerGap(497, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHeaderLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(lblTituloFormuulario)
                .addGap(14, 14, 14))
        );

        getContentPane().add(panelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 45));
        getContentPane().add(txtNombreRuta, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 220, 30));

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 270, 280));

        jLabel1.setText("NOMBRE DE LA RUTA");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));
        getContentPane().add(txtUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 220, 30));

        comboDificultad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fácil", "Media", "Dificil" }));
        comboDificultad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDificultadActionPerformed(evt);
            }
        });
        getContentPane().add(comboDificultad, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 100, 30));

        comboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Senderismo", "Ciclismo", "Escalada" }));
        getContentPane().add(comboTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 110, 30));

        lblUbicacion.setText("UBICACIÓN");
        getContentPane().add(lblUbicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        lblKm.setText("TIPO");
        getContentPane().add(lblKm, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, -1, -1));

        lblDificultad.setText("DIFICULTAD");
        getContentPane().add(lblDificultad, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        txtLongitud.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLongitudActionPerformed(evt);
            }
        });
        getContentPane().add(txtLongitud, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 220, 30));

        jLabel2.setText("LONGITUD (KM)");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        btnCancelar.setText("Cancelar");
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 380, -1, -1));

        btnGuardar.setText("Cuardar");
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 380, -1, -1));

        lblDescripcion.setText("DESCRIPCIÓN");
        getContentPane().add(lblDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboDificultadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDificultadActionPerformed
    }//GEN-LAST:event_comboDificultadActionPerformed

    private void txtLongitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLongitudActionPerformed
    }//GEN-LAST:event_txtLongitudActionPerformed

    // getters para el controlador
    public String getNombreRuta()  { return txtNombreRuta.getText(); }
    public String getUbicacion()   { return txtUbicacion.getText(); }
    public String getDificultad()  { return (String) comboDificultad.getSelectedItem(); }
    public String getTipo()        { return (String) comboTipo.getSelectedItem(); }
    public String getLongitud()    { return txtLongitud.getText(); }
    public String getDescripcion() { return jTextArea1.getText(); }

    public javax.swing.JButton getBtnGuardar()  { return btnGuardar; }
    public javax.swing.JButton getBtnCancelar() { return btnCancelar; }

    public void setTituloFormulario(String s) { lblTituloFormuulario.setText(s); }

    public void cargarDatos(String nombre, String ubic, String dif, String tipo, String lon, String desc) {
        txtNombreRuta.setText(nombre);
        txtUbicacion.setText(ubic);
        comboDificultad.setSelectedItem(dif);
        comboTipo.setSelectedItem(tipo);
        txtLongitud.setText(lon);
        jTextArea1.setText(desc);
    }

    public void mostrarMensaje(String msg) { javax.swing.JOptionPane.showMessageDialog(this, msg); }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> comboDificultad;
    private javax.swing.JComboBox<String> comboTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDificultad;
    private javax.swing.JLabel lblKm;
    private javax.swing.JLabel lblTituloFormuulario;
    private javax.swing.JLabel lblUbicacion;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JTextField txtLongitud;
    private javax.swing.JTextField txtNombreRuta;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables
}
