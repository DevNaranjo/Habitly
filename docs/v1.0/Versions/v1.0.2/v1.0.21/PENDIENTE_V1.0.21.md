Refactorización del Modelo de Propiedad (Etapa 2)



Objetivo: Superar la fase de "Vivienda básica" de la BETA.



Cambios en Vivienda.java: Añadir superficie (double), conservacion (Enum/Int), piscina (boolean) y garaje (boolean).



Impacto en Persistencia: Es necesario actualizar CajaFuerte.java para que el método serializable reconozca los nuevos campos, o de lo contrario el archivo sistema.dat dará error de lectura (StreamCorruptedException).

