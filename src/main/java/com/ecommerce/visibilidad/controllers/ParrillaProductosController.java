package com.ecommerce.visibilidad.controllers;

import com.ecommerce.visibilidad.services.IParrillaProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
public class ParrillaProductosController {

    @Autowired
    IParrillaProductosService parrillaProductosService;

    @PostMapping(path = "/products")
    public String getProductsInStock(
            @RequestParam(name = "product") MultipartFile product,
            @RequestParam(name = "size") MultipartFile size,
            @RequestParam(name = "stock") MultipartFile stock ){

        List<Integer> productsInStock = parrillaProductosService.getProductosEnStock(product, size, stock);

        return productsInStock.toString();
    }

}
