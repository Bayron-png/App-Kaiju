![Lenguaje](https://img.shields.io/badge/Lenguaje-Kotlin-purple)
![Interfaz](https://img.shields.io/badge/Interfaz-JetpackCompose-blue)
# App Kaiju

## Integrantes participantes

| Nombre completo                 | Correo                 |
| ------------------------------- | ---------------------- |
| Luis Antonio Álvarez Requejo    | luis.alvarez@duocuc.cl |
| Mario Ignacio Andrade Salinas   | mar.andrades@duocuc.cl |
| Bayron Alexander Urrutia Flores | bay.urrutia@duocuc.cl  |

## **Caso**
El caso de la tienda Kaiju solicita desarrollar un Producto Mínimo Viable (MVP) para una aplicación móvil Android. Su principal objetivo es automatizar y optimizar el registro, la trazabilidad y la gestión del inventario y ventas, reemplazando el control manual actual por uno actualizado en tiempo real.

## **Funcionalidades implementadas**
La aplicación móvil Kaiju permite consultar y administrar el catálogo de productos de la tienda, incorporando funcionalidades de control de inventario y alertas de stock.

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
- Actualización de las cantidades de stock mediante los siguientes tipos de movimiento:
* **Ingreso:** aumenta la cantidad disponible del producto.
* **Salida:** disminuye la cantidad disponible del producto.
* **Ajuste:** permite corregir las cantidades de inventario según las necesidades de la tienda.
- Registro del motivo de cada movimiento para facilitar el seguimiento de los cambios realizados.
- Consulta del historial de movimientos de cada producto.

### Sistema de alertas de stock
- Identificación de productos sin stock disponible.
- Detección de productos con stock bajo según el mínimo configurado para cada uno.
- Acceso a un apartado de alertas para facilitar la supervisión del inventario y la identificación de productos que requieren reposición.

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

5. Ejecutar la aplicación. Seleccionar la configuración de ejecución correspondiente al módulo de la aplicación, normalmente app, y presionar el botón Run (▶).

6. Probar las funcionalidades. Una vez iniciada la aplicación, acceder al catálogo en modo invitado o utilizar un perfil con permisos de administración para gestionar productos, registrar movimientos y consultar las alertas de inventario.

## **Arquitectura**
Kotlin + Jetpack Compose, patrón MVVM.

## Diseño y planificación
Sketch, wireframe, mockup y demás material de diseño del equipo (Subidos al drive).

### **Carpetas del equipo:**
- [Drive Caso Kaiju](https://drive.google.com/drive/folders/12vOZTJMERUu-XkpzzRBcUCfUwOX1UqIa?usp=drive_link)
- [Tablero Trello](https://trello.com/b/rKnWL8ft/caso-kaiju-desarrollo-apps-moviles)
