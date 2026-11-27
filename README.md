![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-2E9E55?style=flat&logo=androidstudio&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)
# App Kaiju

## Integrantes

| Nombre completo                 | Correo                 |
| ------------------------------- | ---------------------- |
| Luis Antonio Álvarez Requejo    | luis.alvarez@duocuc.cl |
| Mario Ignacio Andrade Salinas   | mar.andrades@duocuc.cl |
| Bayron Alexander Urrutia Flores | bay.urrutia@duocuc.cl  |

**Repositorio Backend:** [Kaiju - Backend](https://github.com/Bayron-png/Kaiju-Backend)

## **Contexto**
El caso de la tienda Kaiju solicita desarrollar una aplicación móvil Android. El objetivo principal  es automatizar y optimizar el registro, la trazabilidad y la gestión del inventario y ventas, reemplazando el control manual actual por uno actualizado en tiempo real.

## **Funcionalidades implementadas**
La aplicación móvil **Kaiju** permite consultar y administrar el catálogo de productos de la tienda, incorporando funcionalidades de control de inventario y alertas de stock:

### Catálogo de productos
- Visualización del catálogo de productos disponibles en la aplicación.
- Acceso al catálogo en modo invitado, permitiendo consultar los productos y sus detalles sin utilizar las funciones de administración.
- Visualización de información de cada producto, incluyendo nombre, SKU, categoría, precio y cantidad disponible.
- Consulta del detalle individual de cada producto.

### Administración del catálogo
- Registro de nuevos productos mediante un formulario.
- Modificación de la información de los productos existentes.
- Eliminación de productos del catálogo.
- Configuración del stock mínimo al momento de registrar un producto.

### Gestión de inventario
- Registro de movimientos de inventario asociados a cada producto.
- Consulta del historial de movimientos de cada producto.
- Actualización de las cantidades de stock mediante los siguientes tipos de movimiento:
1. **Ingreso:** aumenta la cantidad disponible del producto.
2. **Salida:** disminuye la cantidad disponible del producto.

### Sistema de alertas de stock
- Identificación de productos sin stock disponible.
- Detección de productos con stock bajo según el mínimo configurado para cada uno y productos con stock en 0.
- Acceso a un apartado de alertas para facilitar la supervisión del inventario y la identificación de productos que requieren reposición.

## Endpoints
Se implementó un Backend para la gestión de productos y usuarios, además de la API pública **"PokeAPI"**, disponiendo de una amplia base de datos con información de pokemones para uso público. 
|URL del EndPoint                               | Descripción del Endpoint|
|-----------------------------------------------|-------------------------|
|`https://pokeapi.co/api/v2/`                   |Corresponde al Endpoint base del Easter Egg, muestra la barra de búsqueda para ingresar el ID o nombre del pokemón a buscar|
|`https://pokeapi.co/api/v2/pokemon/{nombreOId}`|Muestra el pokemón ingresado en caso de coincidir el ID o nombre con un pokemón registrado en la base de datos|


### Descripción Easter-Egg
La función secreta corresponde a un apartado que se accede clickeando repetidamente el logo de kaiju en la pantalla inicio de sesión. Esta pantalla provee información de pokemones como su:
- ID
- Nombre
- Tipo
- Altura
- Peso
- Imagen del mismo


### Cómo activar el Easter Egg
Al pulsar **10 veces seguidas** en el logo de la pantalla de inicio de sesión (*Login*), se activa el disparador que redirige automáticamente a la pantalla oculta de la Pokédex.

### Cómo probarlo
Una vez activado el Easter Egg y estando en la pantalla de la Pokédex:
1. Asegúrate de que el dispositivo tenga conexión a internet (se valida el permiso de red).
2. Ingresa el nombre de un Pokémon en la barra de búsqueda (por ejemplo: `pikachu`, `charizard`, `bulbasaur`) o su ID numérico (por ejemplo: `25`, `1`).
3. Presiona el botón de **Buscar**.
4. La aplicación consultará la PokeAPI y mostrará en pantalla la tarjeta con el ID, nombre, tipo, altura, peso e imagen del Pokémon consultado.

### Captura del Easter-Egg
<img width="1080" height="2216" alt="Screenshot_2026-11-04-20-57-57-835_cl kaiju appkaiju-edit" src="https://github.com/user-attachments/assets/1641cb7a-4505-489b-8f55-7bc495aefe34" />

## Instrucciones para configurar y ejecutar la aplicación

### Requisitos previos
Antes de ejecutar el proyecto, es necesario contar con:
- Android Studio, instalado en el equipo.
- JDK compatible con la versión de Gradle 9.6.0
- Android SDK.
- Un dispositivo Android con la depuración USB habilitada o un emulador configurado desde Android Studio.

### Pasos para ejecutar el proyecto
1. Obtener el código fuente. Clonar el repositorio del proyecto o descargarlo desde GitHub.
```bash
git clone https://github.com/Bayron-png/App-Kaiju
cd App-Kaiju
```

2. Abrir el proyecto. Iniciar Android Studio y seleccionar Open. Buscar la carpeta raíz del proyecto y abrirla.

3. Sincronizar Gradle. Esperar a que Android Studio descargue las dependencias necesarias y complete la sincronización del proyecto. Si aparece una solicitud para instalar componentes del SDK, instalarlos según corresponda.

4. Configurar el dispositivo. Conectar un dispositivo Android mediante USB con la depuración habilitada o crear e iniciar un emulador desde Device Manager.

5. Ejecutar la aplicación. Seleccionar la configuración de ejecución correspondiente al módulo de la aplicación, normalmente app, y presionar el botón Run.

6. Probar las funcionalidades. Una vez iniciada la aplicación, acceder al catálogo en modo invitado o utilizar un perfil con permisos de administración para gestionar productos, registrar movimientos y consultar las alertas de inventario.

### **Carpetas del equipo:**
- [Drive Caso Kaiju](https://drive.google.com/drive/folders/12vOZTJMERUu-XkpzzRBcUCfUwOX1UqIa?usp=drive_link)
- [Tablero Trello](https://trello.com/b/rKnWL8ft/caso-kaiju-desarrollo-apps-moviles)
- [Diagrama de flujo](https://viewer.diagrams.net/?tags=%7B%7D&lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Diagrama%20de%20flujo%20-%20app%20Kaiju.drawio&dark=0#Uhttps%3A%2F%2Fdrive.google.com%2Fuc%3Fid%3D1uI4tBsdldcoiiz9aATtBcTlo7mZs2bQp%26export%3Ddownload)

## Capturas del APK firmado
