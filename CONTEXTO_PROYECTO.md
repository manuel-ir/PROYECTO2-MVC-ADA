# Contexto del proyecto - TrailMate

## Descripción general

Aplicación de escritorio en Java Swing similar a Wikiloc pero muy simplificada. Permite a los usuarios crear, visualizar, modificar y borrar rutas de senderismo, guardarlas en listas personales y añadir valoraciones y comentarios.

**Nombre de la app:** TrailMate  
**Asignatura:** Acceso a Datos (2º DAM)  
**Repositorio:** `git@github.com:manuel-ir/PROYECTO2-MVC-ADA.git`

---

## Stack técnico

- **Lenguaje:** Java (Maven, Java 23)
- **Interfaz:** Java Swing con AbsoluteLayout (librería NetBeans)
- **BD:** MySQL — base de datos `mvcADA`, usuario `root`, contraseña `1234`
- **Acceso a datos:** JDBC puro (PreparedStatement, CallableStatement, transacciones)
- **IDE:** NetBeans 23
- **Control de versiones:** Git + GitHub

---

## Reglas importantes del proyecto

1. **MVC estricto:** las vistas solo exponen getters/setters y referencias a botones. Toda la lógica va en los controladores.
2. **Sin rastro de IA en GitHub:** comentarios en español y naturales, commits en español casual, sin inglés mezclado salvo términos técnicos.
3. **Estilo de código:** nivel estudiante de 2º DAM. Comentarios cortos y descriptivos.
4. **Commits:** mensajes en español casual. Sin caracteres especiales en comentarios de código.
5. **Sin caracteres especiales** (—, …, etc.) en comentarios Java.

---

## Flujo Git establecido

Para evitar problemas con NetBeans al cambiar de rama, el flujo es:

1. Trabajar siempre en la rama `developer` local
2. Hacer commit en `developer` local
3. Subir a la rama de funcionalidad sin cambiar de rama:
   ```bash
   git push origin HEAD:funcionalidadXXX
   ```
4. Abrir PR en GitHub: `funcionalidadXXX` → `developer`
5. Mergear PR y hacer `git pull origin developer`

Para sincronizar una rama de funcionalidad con el estado actual de developer antes de empezar:
```bash
git push origin origin/developer:refs/heads/funcionalidadXXX --force
```

---

## Estado actual de las ramas

```
main              (estable)
developer         (rama de trabajo, contiene todo lo implementado)
  vistas                      mergeada via PR #1
  funcionalidadRegistro       mergeada via PR #2
  funcionalidadPantallaPrincipal  mergeada via PR #3
  funcionalidadLogin          mergeada via PR #4
  funcionalidadPantallaRuta   en developer (sin PR formal)
  funcionalidadListas         push realizado, PR pendiente
  funcionalidadValoraciones   push realizado, PR pendiente
```

---

## Paleta de colores

| Uso | Color |
|---|---|
| Panel de marca / headers | `#2D5016` (verde forestal) |
| Subtítulo / bienvenida | `#9FBF80` |
| Texto blanco sobre verde | `#FFFFFF` |
| Botón borrar (foreground) | `#A32D2D` |

---

## Base de datos

**Archivo:** `PROYECTO2ADA-PRUEBA/src/main/java/resources/BBDD.sql`

### Tablas

```
USUARIO       (id_usuario PK AI, nombre_usuario UNIQUE, email UNIQUE, password)
RUTA          (id_ruta PK AI, nombre_ruta, descripcion_ruta, ubicacion, dificultad,
               tipo_actividad, longitud DECIMAL, fecha_creacion DEFAULT CURRENT_DATE, id_creador FK→USUARIO)
LISTA         (id_lista PK AI, nombre_lista, id_usuario FK→USUARIO)
RUTA_LISTA    (id_lista FK, id_ruta FK) — PK compuesta
COMENTARIO    (id_usuario FK, id_ruta FK, fecha_comentario DATETIME DEFAULT NOW, contenido) — PK compuesta
VALORACION    (id_usuario FK, id_ruta FK, fecha_valoracion DATETIME DEFAULT NOW,
               puntuacion CHECK 1-5, comentario) — PK compuesta
```

Todas las FK tienen `ON DELETE CASCADE`.

### Procedimientos almacenados

- `calcularMediaValoracion(IN p_id_ruta, OUT p_media)` — usado en `obtenerValoracionMedia()`
- `borrarRutaSiEsCreador(IN p_id_ruta, IN p_id_usuario, OUT p_resultado)` — usado en `borrarRuta()`
- `agregarRutaALista(IN p_id_lista, IN p_id_ruta, IN p_id_usuario, OUT p_resultado)` — usado en `agregarRutaALista()`

---

## Estructura del proyecto

```
PROYECTO2ADA-PRUEBA/              (raiz del repo git)
├── .gitignore
├── CONTEXTO_PROYECTO.md          (no sube a GitHub)
├── docs/
│   └── documentacion.md          (documentacion del proyecto)
└── PROYECTO2ADA-PRUEBA/          (proyecto Maven)
    ├── pom.xml
    └── src/main/java/
        ├── Main.java
        ├── controlador/
        │   ├── ControladorLogin.java
        │   ├── ControladorRegistro.java
        │   ├── ControladorPrincipal.java
        │   ├── ControladorRuta.java
        │   ├── ControladorAgregarRuta.java
        │   └── ControladorLista.java
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
        │   ├── BBDD/
        │   │   ├── ModeloER-MVC.png
        │   │   └── ModeloRelacionalMVC.png
        │   ├── LogoPortada/
        │   │   └── LogoPortada.png
        │   ├── Wireframes/
        │   │   ├── Login.png, Registro.png, PaginaPrincipal.png
        │   │   ├── DetallesRuta.png, Agregar-EditarRuta.png, Listas.png
        │   ├── LogoTrailMate.png        (original)
        │   ├── LogoTrailMate130.png     (130x130 para login)
        │   └── LogoTrailMate60.png      (60x60 para header)
        └── resources/
            └── BBDD.sql
```

---

## Estado actual de los archivos implementados

### Main.java
Arranca la app: crea `GestorBD`, `VistaLogin` y `ControladorLogin`.

### GestorBD.java — métodos implementados

**Conexión:** `jdbc:mysql://localhost:3306/mvcADA`, root, 1234

**Métodos:**
- `validarLogin(email, password)` → int idUsuario (-1 si falla)
- `registrarUsuario(nombre, email, password)` → boolean — **con transaccion**: inserta usuario + crea lista "Favoritos"
- `getNombreUsuario(idUsuario)` → String
- `cargarRutasTabla()` → DefaultTableModel (Nombre, Ubicacion, Dificultad, Valoracion)
- `cargarIdsRutas()` → List<int[]> con [id_ruta, id_creador] por fila
- `obtenerRuta(idRuta)` → Object[] [nombre, desc, ubic, dif, tipo, longitud, id_creador]
- `obtenerValoracionMedia(idRuta)` → String — usa **CallableStatement** (`calcularMediaValoracion`)
- `crearRuta(nombre, desc, ubic, dif, tipo, longitud, idCreador)` → boolean
- `editarRuta(idRuta, nombre, desc, ubic, dif, tipo, longitud)` → boolean
- `borrarRuta(idRuta, idUsuario)` → boolean — usa **CallableStatement** (`borrarRutaSiEsCreador`)
- `cargarComentarios(idRuta)` → DefaultListModel<String>
- `insertarComentario(idUsuario, idRuta, contenido)` → boolean
- `insertarValoracion(idUsuario, idRuta, puntuacion, comentario)` → boolean
- `cargarListasModel(idUsuario)` → DefaultListModel<String>
- `cargarIdsListas(idUsuario)` → List<Integer>
- `cargarRutasEnLista(idLista)` → DefaultListModel<String>
- `cargarIdsRutasEnLista(idLista)` → List<Integer>
- `crearLista(nombre, idUsuario)` → boolean
- `eliminarLista(idLista)` → boolean
- `quitarRutaDeLista(idLista, idRuta)` → boolean
- `agregarRutaALista(idLista, idRuta, idUsuario)` → boolean — usa **CallableStatement** (`agregarRutaALista`)

### Controladores implementados

**ControladorLogin:** valida campos, llama `validarLogin()`, abre `VistaPaginaPrincipal` o muestra error. Botón registro abre `VistaRegistro`.

**ControladorRegistro:** valida nombre (min 3 chars), email (lowercase, @, gmail.com), contraseña (10+ chars, may+min+num), coincidencia. Llama `registrarUsuario()`.

**ControladorPrincipal:** carga tabla al abrir, activa/desactiva botones según si el usuario es creador de la fila seleccionada. Conecta: nueva ruta, ver, editar, borrar, mis listas, cerrar sesion.

**ControladorRuta:** carga datos de la ruta y comentarios. Gestiona: comentar, valorar (dialogo 1-5 + comentario opcional), guardar en lista (dialogo de seleccion).

**ControladorAgregarRuta:** modo nueva (idRuta=0) o edicion (idRuta>0). Valida campos obligatorios y longitud positiva. Guarda o actualiza.

**ControladorLista:** carga listas del usuario. Al seleccionar lista carga sus rutas. Gestiona: nueva lista, eliminar lista (confirmacion), quitar ruta de lista.

---

## Vistas — estado actual

### VistaLogin
- `JPasswordField` (variable mal nombrada, deberia ser `txtPassword`) — cambiar en NetBeans
- Getter password: `return new String(JPasswordField.getPassword())`
- Logo pendiente de mostrar correctamente (`lblLogo` con `LogoTrailMate130.png`)

### VistaRegistro
- Getters implementados fuera del bloque GEN
- `txtPassword` y `txtConfirmar` son JPasswordField

### VistaPaginaPrincipal
- `btnMisListas` añadido a la toolbar (JButton)
- Getter: `getBtnMisListas()` → JButton
- Logo pendiente de mostrar correctamente (`lblLogo` con `LogoTrailMate60.png`)

### VistaRuta
- Getters + setters implementados
- Seccion de comentarios: `listComentarios`, `txtNuevoComentario`, `btnComentar`
- `defaultCloseOperation` = DISPOSE_ON_CLOSE

### VistaFormularioRuta
- Getters implementados, incluye `cargarDatos()` para modo edicion
- `defaultCloseOperation` = DISPOSE_ON_CLOSE
- Typo en variable: `lblTituloFormuulario` (doble u, generado por NetBeans)

### VistaLista
- Dos JList: `listaRutas` (listas del usuario) y `tablaRutaLista` (rutas de la lista)
- Getters: `getListaListas()`, `getTablaRutaLista()`, `setModeloListas()`, `setModeloRutas()`
- `defaultCloseOperation` = DISPOSE_ON_CLOSE

---

## pom.xml — configuracion relevante

- `artifactId`: PROYECTO2ADA
- `maven.compiler.release`: 22
- `exec.mainClass`: Main
- Build resources: incluye `src/main/java` para copiar `.png`, `.jpg`, `.gif`, `.sql` al classpath

---

## Pendiente

- Corregir nombre de variable `JPasswordField` en VistaLogin a `txtPassword` en NetBeans
- Resolver por que los logos no se ven en VistaLogin y VistaPaginaPrincipal
- Subir commits pendientes al repo (valoraciones, listas, pom.xml, vistas)
