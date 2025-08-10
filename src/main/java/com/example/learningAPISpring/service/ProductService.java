package com.example.learningAPISpring.service;

import com.example.learningAPISpring.dto.DTO.ProductDTO;
import com.example.learningAPISpring.entity.Product;

import java.util.List;
import java.util.UUID;
public interface  ProductService {
    public Product addProduct(ProductDTO product);
    public List<ProductDTO> getAllProducts(UUID categoryId, UUID typeId);

    ProductDTO getProductBySlug(String slug);

    ProductDTO getProductById(UUID id);

    Product updateProduct(ProductDTO productDto, UUID id);

    Product fetchProductById(UUID uuid) throws Exception;
}
