package com.example.learningAPISpring.controllers;

import com.example.learningAPISpring.dto.DTO.ProductDTO;
import com.example.learningAPISpring.dto.DTO.ProductFilterDTO;
import com.example.learningAPISpring.entity.Product;
import com.example.learningAPISpring.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth/product")
@CrossOrigin
public class ProductController {
    private ProductService productService;



    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(required = false,name = "categoryId",value = "categoryId") UUID categoryId, @RequestParam(required = false,name = "typeId",value = "typeId") UUID typeId, @RequestParam(required = false) String slug, HttpServletResponse response){
        List<ProductDTO> productList = new ArrayList<>();
        if(StringUtils.isNotBlank(slug)){
            ProductDTO productDto = productService.getProductBySlug(slug);
            productList.add(productDto);
        }
        else {
            productList = productService.getAllProducts(categoryId, typeId);
        }
        response.setHeader("Content-Range",String.valueOf(productList.size()));
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Product>> getProducts(@RequestBody ProductFilterDTO filter) {
        Page<Product> products = productService.getProducts(filter);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id){
        ProductDTO productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    //   create Product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDto){
        Product product = productService.addProduct(productDto);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDTO productDto,@PathVariable UUID id){
        Product product = productService.updateProduct(productDto,id);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
