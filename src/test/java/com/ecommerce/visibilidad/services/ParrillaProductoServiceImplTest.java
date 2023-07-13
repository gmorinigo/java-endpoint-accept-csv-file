package com.ecommerce.visibilidad.services;

import com.ecommerce.visibilidad.exceptions.InvalidFileFormatException;
import com.ecommerce.visibilidad.exceptions.InvalidFileNameException;
import com.ecommerce.visibilidad.services.impl.ParrillaProductoServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_PRODUCT_INVALID_FORMAT_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_PRODUCT_OK_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_SIZE_INVALID_FORMAT_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_SIZE_OK_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_STOCK_INVALID_FORMAT_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_STOCK_OK_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ParrillaProductoServiceImplTest {

    @InjectMocks
    private ParrillaProductoServiceImpl parrillaProductoService;

    MockMultipartFile product, size, stock ;

    public final static List<Integer> productsInStock  = Arrays.asList(5,1,3);

    @SneakyThrows
    @BeforeEach
    void setUp() {
        byte[] csv = Files.readAllBytes(Paths.get(FILE_CSV_PRODUCT_OK_PATH));

        product = new MockMultipartFile("product", "product.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        csv =  Files.readAllBytes(Paths.get(FILE_CSV_SIZE_OK_PATH));

        size = new MockMultipartFile("size", "size.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        csv =  Files.readAllBytes(Paths.get(FILE_CSV_STOCK_OK_PATH));

        stock = new MockMultipartFile("stock", "stock.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);
    }

    @DisplayName("Ok Test With 3 corrects CSV Files")
    @Test
    void getProductosEnStock_OkTest(){
        assertEquals(parrillaProductoService.getProductosEnStock(product, size, stock), productsInStock);
    }

    @Test
    @SneakyThrows
    void invalidFormatProductCsvTest(){

        byte[] csv = Files.readAllBytes(Paths.get(FILE_CSV_PRODUCT_INVALID_FORMAT_PATH));

        product = new MockMultipartFile("product", "product.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        assertThrows(InvalidFileFormatException.class, () -> parrillaProductoService.getProductosEnStock(product, size, stock));
    }

    @Test
    @SneakyThrows
    void invalidFormatSizeCsvTest(){

        byte[] csv = Files.readAllBytes(Paths.get(FILE_CSV_SIZE_INVALID_FORMAT_PATH));

        size = new MockMultipartFile("size", "size.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        assertThrows(InvalidFileFormatException.class, () -> parrillaProductoService.getProductosEnStock(product, size, stock));
    }

    @Test
    @SneakyThrows
    void invalidFormatStockCsvTest(){

        byte[] csv = Files.readAllBytes(Paths.get(FILE_CSV_STOCK_INVALID_FORMAT_PATH));

        stock = new MockMultipartFile("stock", "stock.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        assertThrows(InvalidFileFormatException.class, () -> parrillaProductoService.getProductosEnStock(product, size, stock));
    }

    @Test
    @SneakyThrows
    void invalidNameCsvFileTest(){
        byte[] csv = Files.readAllBytes(Paths.get(FILE_CSV_STOCK_OK_PATH));

        stock = new MockMultipartFile("stock", "stock.vvv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        assertThrows(InvalidFileNameException.class, () -> parrillaProductoService.getProductosEnStock(product, size, stock));
    }
}
