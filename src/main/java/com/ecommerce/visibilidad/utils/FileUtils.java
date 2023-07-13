package com.ecommerce.visibilidad.utils;

import com.ecommerce.visibilidad.constants.ErrorConstants;
import com.ecommerce.visibilidad.exceptions.InvalidFileNameException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static com.ecommerce.visibilidad.constants.FileConstants.FILE_FORMAT_EXTENSION_CSV;

public class FileUtils {
    public static File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();
        return file;
    }

    public static void validateFileExtension(File file) {
        if (!file.getName().endsWith(FILE_FORMAT_EXTENSION_CSV)) {
            throw new InvalidFileNameException(
                    ErrorConstants.FILE_NAME_ERROR_MESSAGE + file.getName() + ErrorConstants.RETRY_ERROR_MESSAGE);
        }
    }

    public static void deleteFile(File file) {
        if (null != file && file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
