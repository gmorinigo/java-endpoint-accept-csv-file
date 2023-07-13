package com.ecommerce.visibilidad.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IParrillaProductosService {
    List<Integer> getProductosEnStock(MultipartFile productMultipart, MultipartFile sizeMultipart, MultipartFile stockMultipart);
}
