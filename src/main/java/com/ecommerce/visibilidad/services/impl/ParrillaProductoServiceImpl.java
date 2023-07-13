package com.ecommerce.visibilidad.services.impl;

import com.ecommerce.visibilidad.constants.ErrorConstants;
import com.ecommerce.visibilidad.dtos.ProductsCsvFileDTO;
import com.ecommerce.visibilidad.dtos.SizeCsvFileDTO;
import com.ecommerce.visibilidad.dtos.StockCsvFileDTO;
import com.ecommerce.visibilidad.exceptions.InvalidFileFormatException;
import com.ecommerce.visibilidad.models.Product;
import com.ecommerce.visibilidad.models.Size;
import com.ecommerce.visibilidad.services.IParrillaProductosService;
import com.ecommerce.visibilidad.utils.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.visibilidad.constants.FileConstants.FIELD_SEPARATOR;
import static com.ecommerce.visibilidad.utils.FileUtils.deleteFile;
import static com.ecommerce.visibilidad.utils.FileUtils.validateFileExtension;

@Service
public class ParrillaProductoServiceImpl implements IParrillaProductosService {

    @Override
    public List<Integer> getProductosEnStock(MultipartFile productMultipart, MultipartFile sizeMultipart, MultipartFile stockMultipart) {
        File productsFile = null, sizeFile = null, stockFile = null;
        List <Product> productsInStock = new ArrayList<>();

        try {
            productsFile = FileUtils.convertMultiPartFileToFile(productMultipart);
            validateFileExtension(productsFile);
            sizeFile = FileUtils.convertMultiPartFileToFile(sizeMultipart);
            validateFileExtension(sizeFile);
            stockFile = FileUtils.convertMultiPartFileToFile(stockMultipart);
            validateFileExtension(stockFile);

            List<ProductsCsvFileDTO> productsDto = getProductsFromCsvFile(productsFile);
            List<SizeCsvFileDTO> sizes = getSizesFromCsvFile(sizeFile);
            List<StockCsvFileDTO> stocks = getStockFromCsvFile(stockFile);

            productsInStock = filterProductsInStock(productsDto, sizes,stocks);
            productsInStock.sort( (p1,p2) -> p1.getSequence().compareTo(p2.getSequence()) );

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            deleteFile(productsFile);
            deleteFile(sizeFile);
            deleteFile(stockFile);
        }
        return productsInStock.stream().map(product -> product.getProductId()).collect(Collectors.toList());
    }

    private List<Product> filterProductsInStock(List<ProductsCsvFileDTO> productsDto, List<SizeCsvFileDTO> sizes, List<StockCsvFileDTO> stocks) {
        List <Product> productsInStock = new ArrayList<>();
        for (ProductsCsvFileDTO productDto : productsDto ){
            List<SizeCsvFileDTO> sizesOfProduct = sizes.stream().filter(size -> size.getProductId().equals(productDto.getProductId()))
                    .collect(Collectors.toList());

            List<Size> sizesWithStock = addStockToSizes(sizesOfProduct, stocks);

            Product aProduct = Product.builder()
                    .productId(productDto.getProductId())
                    .sequence(productDto.getSequence())
                    .sizes(sizesWithStock)
                    .build();

            if (productWithStockOrSpecialCasuistry(aProduct))
                productsInStock.add(aProduct);
        }
        return productsInStock;
    }

    private boolean productWithStockOrSpecialCasuistry(Product aProduct) {
        return (aProduct.hasSpecialSize() && aProduct.hasSpecialSizeWithStock() && aProduct.hasNonSpecialSizeWithStock())
            || (aProduct.hasSizeBackSoon() && aProduct.doesntHasSpecialSize())
            || (aProduct.doesntHasSpecialSize() && aProduct.hasNonSpecialSizeWithStock());
    }

    private List<Size> addStockToSizes(List<SizeCsvFileDTO> sizesOfProduct, List<StockCsvFileDTO> stocks) {
        List<Size> sizesWithStock = new ArrayList<>();
        for (SizeCsvFileDTO sizeOfProduct : sizesOfProduct){
            Integer quantity = stocks.stream().filter(stock -> stock.getSizeId().compareTo(sizeOfProduct.getSizeId()) == 0 )
                    .map(stock -> stock.getQuantity()).findFirst().orElse(0);

            sizesWithStock.add(Size.builder()
                    .productId(sizeOfProduct.getProductId())
                    .backSoon(sizeOfProduct.isBackSoon())
                    .special(sizeOfProduct.isSpecial())
                    .quantity(quantity)
                    .build());
        }
        return sizesWithStock;
    }

    private List<ProductsCsvFileDTO> getProductsFromCsvFile(File productsFile) {
        List<ProductsCsvFileDTO> processedLines = new ArrayList<>();
        for (String line : getLinesOfFile(productsFile)) {
            final String[] lineArray = line.trim().split(FIELD_SEPARATOR);
            if (lineArray.length != 2) {
                throw new InvalidFileFormatException(ErrorConstants.FILE_FORMAT_ERROR_MESSAGE +
                        productsFile.getName() + ErrorConstants.RETRY_ERROR_MESSAGE);
            }
            processedLines.add(new ProductsCsvFileDTO(Integer.parseInt(lineArray[0]),Integer.parseInt(lineArray[1])));
        }
        return processedLines;
    }

    private List<SizeCsvFileDTO> getSizesFromCsvFile(File sizeFile) {
        List<SizeCsvFileDTO> processedLines = new ArrayList<>();
        for (String line : getLinesOfFile(sizeFile)) {
            final String[] lineArray = line.trim().replace(" ","").split(FIELD_SEPARATOR);
            if (lineArray.length != 4) {
                throw new InvalidFileFormatException(ErrorConstants.FILE_FORMAT_ERROR_MESSAGE +
                        sizeFile.getName() + ErrorConstants.RETRY_ERROR_MESSAGE);
            }
            processedLines.add(new SizeCsvFileDTO(Integer.parseInt(lineArray[0]),Integer.parseInt(lineArray[1])
                    ,lineArray[2].equals("true"),lineArray[3].equals("true")));
        }
        return processedLines;
    }

    private List<StockCsvFileDTO> getStockFromCsvFile(File stockFile) {
        List<StockCsvFileDTO> processedLines = new ArrayList<>();
        for (String line : getLinesOfFile(stockFile)) {
            final String[] lineArray = line.trim().split(FIELD_SEPARATOR);
            if (lineArray.length != 2) {
                throw new InvalidFileFormatException(ErrorConstants.FILE_FORMAT_ERROR_MESSAGE +
                        stockFile.getName() + ErrorConstants.RETRY_ERROR_MESSAGE);
            }
            processedLines.add(new StockCsvFileDTO(Integer.parseInt(lineArray[0]),Integer.parseInt(lineArray[1])));
        }
        return processedLines;
    }

    private List<String> getLinesOfFile(File file) {
        List<String> lines = new ArrayList<>();
        try (LineIterator iterator = org.apache.commons.io.FileUtils.lineIterator(file, "UTF-8")) {
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }


}
