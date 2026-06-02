# Contexto del proyecto - TrailMate

## Descripción general

Aplicación de escritorio en Java Swing similar a Wikiloc pero muy simplificada. Permite a los usuarios crear, visualizar, modificar y borrar rutas de senderismo, guardarlas en listas personales y añadir valoraciones/comentarios.

**Nombre de la app:** TrailMate  
**Asignatura:** Acceso a Datos (2º DAM)  
**Repositorio:** `git@github.com:manuel-ir/PROYECTO2-MVC-ADA.git`

---

## Stack técnico

- **Lenguaje:** Java (Maven, Java 22)
- **Interfaz:** Java Swing con AbsoluteLayout (librería NetBeans)
- **BD:** MySQL — base de datos `mvcADA`, usuario `root`, contraseña `1234`
- **Acceso a datos:** JDBC puro (sin Hibernate)
- **IDE:** NetBeans
- **Control de versiones:** Git + GitHub

---

## Reglas importantes del proyecto

1. **MVC estricto:** las vistas no pueden tener lógica. Solo exponen getters/setters y referencias a botones. Toda la lógica va en los controladores.
2. **Sin rastro de IA en GitHub:** comentarios en español y naturales, commits en español casual, sin inglés mezclado salvo términos técnicos, sin archivos de configuración extraños.
3. **Estilo de código:** nivel estudiante de 2º DAM. Comentarios cortos tipo `// pendiente` o `// carga datos de la BD`.
4. **Commits:** mensajes en español imperativo casual. Ejemplo: `"Añado controlador de registro"`, no `"feat: add registration controller"`.

---

## Paleta de colores

| Uso | Color |
|---|---|
| Panel de marca / headers | `#2D5016` (verde forestal) |
| Subtítulo app | `#9FBF80` |
| Texto blanco sobre verde | `#FFFFFF` |
| Botón borrar | `#A32D2D` (foreground) |
| Fondo formularios | blanco por defecto de Swing |

---

## Base de datos

**Archivo:** `PROYECTO2ADA-PRUEBA/src/main/java/resources/BBDD.sql`

### Tablas

```
USUARIO (id_usuario PK, nombre_usuario UNIQUE, email UNIQUE, password)
RUTA (id_ruta PK, nombre_ruta, descripcion_ruta, ubicacion, dificultad, tipo_actividad, longitud, fecha_creacion DEFAULT CURRENT_DATE, id_creador FK→USUARIO)
LISTA (id_lista PK, nombre_lista, id_usuario FK→USUARIO)
RUTA_LISTA (id_lista FK, id_ruta FK) — PK compuesta, tabla de unión M:N
VALORACION (id_usuario FK, id_ruta FK, fecha_valoracion DATETIME DEFAULT NOW, puntuacion CHECK 1-5, comentario) — PK compuesta (id_usuario, id_ruta, fecha_valoracion)
```

Todas las FK tienen `ON DELETE CASCADE`.

---

## Estructura del proyecto

```
PROYECTO2ADA-PRUEBA/
├── .gitignore
├── CONTEXTO_PROYECTO.md        ← este archivo (no sube a GitHub)
└── PROYECTO2ADA-PRUEBA/        ← proyecto Maven
    ├── pom.xml
    └── src/main/java/
        ├── Main.java
        ├── controlador/
        │   ├── ControladorLogin.java
        │   ├── ControladorRegistro.java
        │   ├── ControladorPrincipal.java
        │   ├── ControladorRuta.java
        │   └── ControladorAgregarRuta.java
        ├── modelo/
        │   └── GestorBD.java
        ├── vista/
        │   ├── VistaLogin.java / .form
        │   ├── VistaRegistro.java / .form
        │   ├── VistaPaginaPrincipal.java / .form
        │   ├── VistaRuta.java / .form
        │   ├── VistaFormularioRuta.java / .form
        │   └── VistaLista.java / .form
        ├── img/
        │   ├── LogoTrailMate.png        (401x559 original)
        │   ├── LogoTrailMate130.png     (130x130 para login)
        │   └── LogoTrailMate60.png      (60x60 para header)
        └── resources/
            └── BBDD.sql
```

---

## Estado actual de los archivos

### GestorBD.java
Solo tiene el constructor con la conexión. **Sin ningún método todavía.**

```java
// Conexión: jdbc:mysql://localhost:3306/mvcADA, root, 1234
```

### Main.java
Solo el método `main` vacío con comentario `// pendiente`.

### Controladores
Todos vacíos con comentario `// pendiente`. Ninguno implementado aún.

---

## Vistas — componentes relevantes por vista

### VistaLogin (550x380 aprox)
- `PanelMarca` — JPanel verde #2D5016, AbsoluteLayout
  - `lblNombreApp` — "TrailMate", Segoe UI Bold 20, blanco
  - `lblSubtituloApp` — "rutas de senderismo", Segoe UI Bold 20, #9FBF80
  - `lblLogo` — ImageIcon `/img/LogoTrailMate130.png`
- `PanelDerecho` — JPanel blanco, AbsoluteLayout
  - `lblEmail`, `txtEmail` (JTextField)
  - `lblPassword`, `txtPassword` (JTextField — **ojo: hay que cambiar a JPasswordField**)
  - `btnLogin` — verde #2D5016, blanco, focusPainted false
  - `btnRegistro` — texto "¿No tienes cuenta? Registrate.", borderPainted false

**Getters que necesita el controlador:**
```java
public String getEmail() { return txtEmail.getText(); }
public String getPassword() { return new String(txtPassword.getPassword()); }
public JButton getBtnLogin() { return btnLogin; }
public JButton getBtnRegistro() { return btnRegistro; }
public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
```

### VistaRegistro (710x380 aprox)
- `panelImagen` — JPanel verde #3A5A1C, GroupLayout
  - `lblFotoRegistro` — reserva para foto panorámica
  - `lblSlogan` — "Únete a la comunidad"
- `PanelDerecho` — GroupLayout
  - `txtNombre`, `txtEmail`, `txtPassword` (JPasswordField), `txtConfirmar` (JPasswordField)
  - `btnRegistrar` — verde, blanco
  - `jLabel1..4` — etiquetas de campo (sin renombrar todavía)

**Getters que necesita el controlador:**
```java
public String getNombre() { return txtNombre.getText(); }
public String getEmail() { return txtEmail.getText(); }
public String getPass() { return new String(txtPassword.getPassword()); }
public String getPassConfirm() { return new String(txtConfirmar.getPassword()); }
public JButton getBtnRegistrar() { return btnRegistrar; }
public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
```

### VistaPaginaPrincipal (800x550)
- `panelHeader` — verde #2D5016, GroupLayout interno
  - `lblLogo` — `/img/LogoTrailMate60.png`
  - `lblNombreApp` — "TrailMate", Segoe UI Bold 16, blanco
  - `lblBienvenida` — "Hola, [nombre]", Segoe UI Bold 14, #9FBF80
  - `btnCerrarSesion`
- `toolBar` — JToolBar gris #CCCCCC
  - `btnNuevaRuta`, `btnVerRuta`, `btnEditarRuta`, `btnBorrarRuta`
- `scrollTabla` → `tablaRutas` (JTable) — columnas: Nombre, Ubicación, Dificultad, Valoración

**Getters que necesita el controlador:**
```java
public JTable getTablaRutas() { return tablaRutas; }
public JButton getBtnNuevaRuta() { return btnNuevaRuta; }
public JButton getBtnVerRuta() { return btnVerRuta; }
public JButton getBtnEditarRuta() { return btnEditarRuta; }
public JButton getBtnBorrarRuta() { return btnBorrarRuta; }
public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
public void setNombreUsuario(String nombre) { lblBienvenida.setText("Hola, " + nombre); }
public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
```

### VistaRuta (~500x420)
- `lblFotoRuta` — reserva foto, borde negro
- `lblNombreRuta` — Segoe UI Bold 18
- `lblDificultad`
- `lblAutor`
- Tres paneles chip con dos labels cada uno:
  - `panelLongitud` → `lblValorLongitud` (Bold 14) + `lblLongitud` ("longitud")
  - `panelUbicacion` → `lblValorUbicacion` (Bold 14) + `lblUbicacion` ("ubicación")
  - `panelValoracion` → `lblValorValoracion` (Bold 14) + `lblValoracion` ("valoración")
- `JScrollPane` → `areaDescripcion` (JTextArea, editable false, lineWrap true)
- `btnValorar`, `btnGuaardarEnLista` (**typo: doble 'a'**), `btnCerrar` (verde)

**Getters que necesita el controlador:**
```java
public void setNombreRuta(String s) { lblNombreRuta.setText(s); }
public void setDificultad(String s) { lblDificultad.setText(s); }
public void setAutor(String s) { lblAutor.setText("Por: " + s); }
public void setLongitud(String s) { lblValorLongitud.setText(s); }
public void setUbicacion(String s) { lblValorUbicacion.setText(s); }
public void setValoracion(String s) { lblValorValoracion.setText(s); }
public void setDescripcion(String s) { areaDescripcion.setText(s); }
public JButton getBtnCerrar() { return btnCerrar; }
public JButton getBtnValorar() { return btnValorar; }
public JButton getBtnGuardarEnLista() { return btnGuaardarEnLista; }
```

### VistaFormularioRuta (~600x420)
- `panelHeader` — verde, GroupLayout, `lblTituloFormuulario` (**typo: doble 'u'**)
- `jLabel1` — "NOMBRE DE LA RUTA" (sin renombrar)
- `txtNombreRuta`, `txtUbicacion`, `txtLongitud`
- `comboDificultad` — items: Fácil, Media, Dificil (**falta tilde en Difícil**)
- `comboTipo` — items: Senderismo, Ciclismo, Escalada
- `jScrollPane1` → `jTextArea1` (descripción, sin renombrar)
- `jLabel2` — "LONGITUD (KM)"
- `btnCancelar`, `btnGuardar` — **typo: texto "Cuardar" en vez de "Guardar"** (pendiente corregir)

**Getters que necesita el controlador:**
```java
public String getNombreRuta() { return txtNombreRuta.getText(); }
public String getUbicacion() { return txtUbicacion.getText(); }
public String getDificultad() { return (String) comboDificultad.getSelectedItem(); }
public String getTipo() { return (String) comboTipo.getSelectedItem(); }
public String getLongitud() { return txtLongitud.getText(); }
public String getDescripcion() { return jTextArea1.getText(); }
public JButton getBtnGuardar() { return btnGuardar; }
public JButton getBtnCancelar() { return btnCancelar; }
public void setTituloFormulario(String s) { lblTituloFormuulario.setText(s); }
public void cargarDatos(String nombre, String ubic, String dif, String tipo, String lon, String desc) { ... }
public void mostrarMensaje(String msg) { JOptionPane.showMessageDialog(this, msg); }
```

### VistaLista
Creada en NetBeans pero sin componentes definidos todavía. Estructura prevista:
- Header verde con título "Mis listas" y `btnNuevaLista`
- Panel izquierdo: `JScrollPane` con `JList` (`listaListas`) — muestra los nombres de las listas del usuario
- Panel derecho: `JScrollPane` con `JTable` (`tablaRutasLista`) — muestra las rutas de la lista seleccionada
- Botones: `btnEliminarLista`, `btnQuitarRuta`, `btnCerrar`

---

## Flujo Git

```
main (rama estable)
  └── developer (rama de integración)
        ├── vistas (mergeada ✓)
        ├── funcionalidadRegistro (pendiente)
        ├── funcionalidadLogin (pendiente)
        ├── funcionalidadPantallaPrincipal (pendiente)
        ├── funcionalidadPantallaRuta (pendiente)
        ├── funcionalidadListas (pendiente)
        └── funcionalidadValoraciones (pendiente)
```

**Flujo por funcionalidad:**
1. `git checkout developer && git checkout -b funcionalidadXXX`
2. Implementar código
3. `git add . && git commit -m "mensaje en español"`
4. `git push origin funcionalidadXXX`
5. Abrir PR en GitHub: `funcionalidadXXX` → `developer`
6. Mergear PR

---

## Orden de implementación de funcionalidades

1. **VistaRegistro + GestorBD.registrarUsuario()** → rama `funcionalidadRegistro`
2. **VistaLogin + GestorBD.validarLogin() + Main.java** → rama `funcionalidadLogin`
3. **VistaPaginaPrincipal + GestorBD.cargarRutasTabla()** → rama `funcionalidadPantallaPrincipal`
4. **VistaRuta + VistaFormularioRuta + CRUD rutas** → rama `funcionalidadPantallaRuta`
5. **VistaLista + métodos de listas** → rama `funcionalidadListas`
6. **Valoraciones** → rama `funcionalidadValoraciones`

---

## Pendiente antes de implementar controladores

- Añadir getters/setters a todas las vistas (fuera del bloque GEN)
- Corregir typos en vistas: "Cuardar", doble 'u' en `lblTituloFormuulario`, doble 'a' en `btnGuaardarEnLista`, tilde en "Dificil"
- `txtPassword` en VistaLogin debería ser JPasswordField, no JTextField

---

## Documentación pendiente (archivo .md en el repo)

Secciones a completar:
- **a. Definición del problema** — descripción de la app y wireframes (capturas de las vistas reales)
- **b.i. Modelo ER** — imagen pendiente de adjuntar por el usuario
- **b.ii. Modelo relacional** — imagen pendiente (captura de MySQL Workbench)
- **d. Control de versiones** — captura de las ramas en GitHub (pendiente crear ramas primero)
- **a. Estructura del proyecto** — árbol de paquetes y explicación MVC
- **b. Funcionamiento básico** — flujo login → registro → pantalla principal → CRUD
- **c. Implementación diseño de datos** — explicación de las tablas y relaciones

La documentación irá en una carpeta `docs/` dentro del repo, en un archivo `documentacion.md`.

---

## Dependencias Maven (pom.xml)

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
<dependency>
    <groupId>org.netbeans.external</groupId>
    <artifactId>AbsoluteLayout</artifactId>
    <version>RELEASE120</version>
</dependency>
```

Java 22, empaquetado como JAR.
