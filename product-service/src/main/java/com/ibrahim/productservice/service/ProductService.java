package com.ibrahim.productservice.service;

import com.ibrahim.productservice.dto.ProductDto;
import com.ibrahim.productservice.model.Product;
import com.ibrahim.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    private final ProductRepository productRepository;

    //Add Product to Database
    public ProductDto createProduct(ProductDto productDto ){
        Product product= Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} has been saved", product.getId());
        return  productDto;
    }

    public List<ProductDto> getAllProducts() {
       List<Product> products= productRepository.findAll();
      return products.stream().map(this::mapToProductDto).collect(Collectors.toList());
    }

    private ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
