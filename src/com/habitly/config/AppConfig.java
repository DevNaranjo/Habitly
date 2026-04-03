package com.habitly.config;

import java.io.File;

public class AppConfig {

    //Tasa del Impuesto General Indirecto Canario aplicable (7.0%).
    public static final double IGIC = 7.0;

    // Nombre de la carpeta y del archivo
    private static final String APP_FOLDER = "Habitly";
    private static final String FILE_NAME = "inventario_habitly.dat";

    //Nombre del archivo binario donde se persistirán los datos.
    public static final String NOMBRE_ARCHIVO = "inventario_habitly.dat";

    //Construye la ruta de la carpeta según el Sistema Operativo.
    public static String getStoragePath() {
        String userHome = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();
        String path;

        if (os.contains("win")) {
            // En Windows: C:\Users\Nombre\AppData\Roaming\Habitly
            // Usamos System.getenv("APPDATA") que apunta directo a Roaming
            path = System.getenv("APPDATA") + File.separator + APP_FOLDER;
        } else {
            // En Linux/Mac: /home/nombre/.habitly (la carpeta empieza por punto para ser oculta)
            path = userHome + File.separator + "." + APP_FOLDER.toLowerCase();
        }

        return path;
    }

     // Devuelve la ruta completa incluyendo el nombre del archivo.
    public static String getFullFilePath() {
        return getStoragePath() + File.separator + FILE_NAME;
    }

    /**
     * Se asegura de que la carpeta de destino exista en el disco duro.
     * Si no existe, la crea.
     */
    public static void asegurarDirectorio() {
        File directorio = new File(getStoragePath());

        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Configuración inicial completada.");
            } else {
                System.err.println("ERROR CRÍTICO: No hay permisos para guardar datos.");
            }
        }
    }
}