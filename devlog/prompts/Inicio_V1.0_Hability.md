CONTEXTO DEL PROYECTO:
Hola. Vamos a retomar el desarrollo de Habitly, un sistema de gestión inmobiliaria especializado en el mercado canario. El proyecto ha sido migrado con éxito de una fase BETA (BlueJ) a un entorno profesional de IntelliJ IDEA + GitHub.

ESTADO DE LA ARQUITECTURA (C:...\GitHub\Habitly):
El repositorio está organizado bajo estándares profesionales. Debes respetar estrictamente esta estructura de directorios:

/src: Código fuente activo.

com.habitly.model: Clases de datos (Vivienda, Casa, Piso).

com.habitly.ui: Lógica de interfaz y Main (GestionInmobiliaria).

/docs: Documentación oficial.

Contiene la Memoria_Principal_Habitly.pdf y la carpeta /editables con el RoadMap_Habitly.ods.

/devlog: Bitácora de desarrollo.

Contiene /diarios de progreso y /prompts con instrucciones maestras.

/archive: Histórico inmutable de la fase BETA de BlueJ.

REGLAS DE CODIFICACIÓN PARA LA V1.0:

Paquetes: Todo código nuevo debe incluir la declaración de package correspondiente.

Clean Code: Vamos a profesionalizar el código (uso de Enums para estados, constantes para el IGIC 7%, y manejo de excepciones).

Persistencia: El objetivo de esta fase es implementar el guardado y carga de datos.

Entorno: Java moderno en IntelliJ. Nada de archivos .ctxt o carpetas de BlueJ.

TAREA ACTUAL:
Estamos listos para empezar. Por favor, confirma que comprendes esta nueva estructura y dime qué archivos necesitas que te facilite para empezar a trabajar en la V1.0.