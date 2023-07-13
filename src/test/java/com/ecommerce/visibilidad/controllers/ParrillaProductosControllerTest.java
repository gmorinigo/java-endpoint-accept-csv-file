package com.ecommerce.visibilidad.controllers;

import com.ecommerce.visibilidad.services.IParrillaProductosService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_PRODUCT_OK_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_SIZE_OK_PATH;
import static com.ecommerce.visibilidad.constants.FileConstants.FILE_CSV_STOCK_OK_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ParrillaProductosControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    ParrillaProductosControllerTest parrillaProductosController;

    @MockBean
    IParrillaProductosService parrillaProductosService;

    @SneakyThrows
    @Test
    void whenGetProducts_thenReturnStatusOk() {
        byte[] csv = Files.readAllBytes(Paths.get(FILE_CSV_PRODUCT_OK_PATH));

        MockMultipartFile product = new MockMultipartFile("product", "product.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        csv =  Files.readAllBytes(Paths.get(FILE_CSV_SIZE_OK_PATH));

        MockMultipartFile size = new MockMultipartFile("size", "size.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        csv =  Files.readAllBytes(Paths.get(FILE_CSV_STOCK_OK_PATH));

        MockMultipartFile stock = new MockMultipartFile("stock", "stock.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE, csv);

        when(parrillaProductosService.getProductosEnStock(any(),any(),any())).thenReturn(List.of(1));

        mvc.perform(multipart("/api/v0/products")
                        .file(product)
                        .file(size)
                        .file(stock)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
                //.andDo(MockMvcResultHandlers.print());

        verify(parrillaProductosService, times(1)).getProductosEnStock(any(),any(),any());

    }

}
