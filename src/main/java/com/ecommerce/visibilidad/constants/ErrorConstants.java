package com.ecommerce.visibilidad.constants;

public final class ErrorConstants {

    public static final String FILE_NAME_ERROR_MESSAGE = "El archivo no es formato .csv. Nombre del archivo ingresado: ";
    public static final String RETRY_ERROR_MESSAGE = " Revisalo y vuelve a intentarlo.";
    public static final String FILE_FORMAT_ERROR_MESSAGE = "El archivo contiene registro/s menor cantidad de columnas de las esperadas. Nombre del archivo ingresado: ";

    public static final String INVALID_FILE_NAME_ERROR_CODE = "INVALID_FILE_NAME";
    public static final String INVALID_FILE_FORMAT_ERROR_CODE = "INVALID_FILE_FORMAT";


    private ErrorConstants() {
    }
}
