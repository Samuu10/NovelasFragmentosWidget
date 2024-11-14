## GESTIÓN NOVELAS AVANZADO CON FRAGMENTOS Y WIDGETS

# Objetivo

El objetivo del proyecto es ampliar la funcionalidad de las prácticas anteriores de Gestion de Novelas.
En este caso se pedía implemnetar la app con fragmentos para crear interfaces de usuario modulares y mejorar el rendimiento de la app.
También se pedía el uso de widgets para permitir la interacción desde la pantalla de inicio del dispositivo.

# Firebase

La aplicación utiliza Firebase como base de datos para almacenar las novelas,
base de datos a la que se han añadido 20 novelas de ejemplo para probar la applicación
`FirebaseHelper` gestiona todas las operaciones relacionadas con la base de datos,
permitiendo que las novelas se sincronicen y persistan a través de diferentes sesiones de la aplicación.

**Novelas añadidas a Firebase como ejemplo** (IMPORTANTE ESCRIBIR EL TITULO LITERAL AL AÑADIR NOVELA para probar la aplicación.)

   - Don Quijote de la Mancha
   - La Odisea
   - La Iliada
   - Crimen y Castigo
   - Orgullo y Prejuicio
   - El Retrato de Dorian Gray
   - Matar a un ruiseñor
   - 1984
   - Moby Dick
   - El Gran Gatsby
   - Ulises
   - Madame Bovary
   - La Metamorfosis
   - En busca del tiempo perdido
   - El guardián entre el centeno
   - El señor de los anillos
   - Cien Años de Soledad
   - Fahrenheit 451
   - Las uvas de la ira
   - El amor en los tiempos del cólera

# Clases JAVA

1. **MainActivity**:
    - Controla la actividad principal de la aplicación, donde se muestran las novelas.
    - Maneja la lógica para añadir y eliminar novelas, así como para mostrar detalles de cada novela.
    - Contiene métodos para interactuar con el `ListView` de novelas y gestionar la sincronización con Firebase.

2. **FirebaseHelper**:
    - Realiza operaciones con Firebase.
    - Contiene métodos para cargar novelas desde Firebase en segundo plano.

3. **PreferencesManager**:
    - Gestiona las preferencias de la aplicación del usuario.
    - Contiene métodos para cargar y guardar novelas en `SharedPreferences`.
    - Notifica al widget sobre las actualizaciones de las novelas.

4. **DetallesNovelasFragment**:
    - Muestra los detalles de una novela seleccionada.
    - Permite marcar o desmarcar una novela como favorita.

5. **ListaFavoritasFragment**:
    - Muestra la lista de novelas favoritas.
    - Actualiza la lista de novelas favoritas cuando hay cambios.

6. **ListaNovelasFragment**:
    - Muestra la lista de todas las novelas.
    - Actualiza la lista de novelas cuando hay cambios.

7. **Novela**:
    - Representa una novela.
    - Implementa `Parcelable` para pasar objetos entre actividades.

8. **NovelaAdapter**:
    - Adaptador para mostrar la lista de novelas en un `RecyclerView`.
    - Maneja el evento de clic en un elemento de la lista.

9. **NovelasFavoritasWidget**:
    - Representa el widget de la aplicación.
    - Controla la actualización del widget y la interacción con el usuario.

10. **WidgetRemoteViews**:
    - Factoria de vistas remotas del widget.
    - Crea y gestiona las vistas del widget.

11. **WidgetService**:
    - Servicio del widget.
    - Crea la factoria de vistas remotas.

12. **WidgetUpdateService**:
    - Servicio de actualización del widget.
    - Ejecuta el trabajo en segundo plano para actualizar el widget.

13. **WidgetUpdateTask**:
    - Gestiona la actualización del widget en segundo plano.
    - Carga las novelas favoritas y actualiza el widget.

# Archivos XML

1. **activity_main.xml**:
    - Define el layout de la actividad principal.
    - Contiene un `FrameLayout` para los fragmentos y botones para añadir, eliminar y cambiar la lista de novelas.

2. **agregar_novela.xml**:
    - Define el layout del diálogo para añadir una novela.
    - Contiene un `EditText` para ingresar el título de la novela.

3. **eliminar_novela.xml**:
    - Define el layout del diálogo para eliminar una novela.
    - Contiene un `EditText` para ingresar el título de la novela a eliminar.

4. **fragment_detalles_novela.xml**:
    - Define el layout del fragmento que muestra los detalles de una novela.
    - Contiene `TextViews` para mostrar la información de la novela y un `CheckBox` para marcarla como favorita.

5. **fragment_lista_favoritas.xml**:
    - Define el layout del fragmento que muestra la lista de novelas favoritas.
    - Contiene un `RecyclerView` para mostrar las novelas favoritas.

6. **fragment_lista_novelas.xml**:
    - Define el layout del fragmento que muestra la lista de todas las novelas.
    - Contiene un `RecyclerView` para mostrar las novelas.

7. **item_novela.xml**:
    - Define el layout de un elemento de la lista de novelas.
    - Contiene `TextViews` para mostrar el título y el autor de la novela.

8. **widget.xml**:
    - Define el layout del widget de la aplicación.
    - Contiene un `TextView` para el título del widget, un `ListView` para las novelas favoritas y un `Button` para abrir la aplicación.

9. **widget_novela_item.xml**:
    - Define el layout de un elemento de la lista de novelas en el widget.
    - Contiene un `TextView` para mostrar el título de la novela.

Link al repositorio: https://github.com/Samuu10/NovelasFragmentosWidget.git
