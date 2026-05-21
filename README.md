# IPCProject

Proyecto de la asignatura **Interfaces Persona-Computador (IPC)** desarrollado con **JavaFX**.

La aplicación es una demo interactiva de mapas que permite cargar una imagen, navegar por ella, hacer zoom y añadir puntos de interés sobre el mapa. Sirve como base para practicar conceptos de interfaz gráfica, eventos de ratón, componentes JavaFX, separación MVC y gestión de vistas con FXML.

## Funcionalidades

- Visualización de mapas a partir de imágenes locales.
- Zoom mediante slider y botones.
- Desplazamiento por el mapa con barras de scroll.
- Añadir puntos de interés (POI) sobre el mapa.
- Añadir anotaciones circulares.
- Listado lateral de POIs añadidos.
- Centrado animado del mapa al seleccionar un POI.
- Visualización de coordenadas del ratón para facilitar la depuración.

## Estructura del proyecto

```text
src/mapademo/
  MapaDemoApp.java              Aplicación principal JavaFX
  FXMLDocument.fxml             Vista principal
  FXMLDocumentController.java   Controlador de la interfaz
  Poi.java                      Modelo de punto de interés

maps/
  upv.jpg
  valencia.jpg
  pirineos.jpg
  calderona.jpg

src/resources/
  estilos.css
  logo.png
  upv.jpg
  calderona.jpg
```

## Tecnologías

- Java 11
- JavaFX
- FXML
- NetBeans / Ant

## Cómo ejecutarlo

1. Abrir el proyecto con NetBeans.
2. Comprobar que JavaFX está configurado en las librerías del proyecto.
3. Ejecutar la clase principal:

```text
mapademo.MapaDemoApp
```

El proyecto incluye un `build.xml`, por lo que también puede compilarse como proyecto Ant si el entorno tiene configuradas las dependencias de JavaFX.

## Estado actual

El proyecto contiene la demo principal de mapas y POIs. La clase `MapaDemoApp` incluye referencias a futuras vistas como login, dashboard, actividades, mapas y perfil, por lo que el repositorio puede estar preparado para ampliarse hacia una aplicación más completa.
