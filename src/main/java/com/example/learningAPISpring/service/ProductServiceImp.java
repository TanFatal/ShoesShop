package com.example.learningAPISpring.service;
import com.example.learningAPISpring.dto.DTO.ProductDTO;
import com.example.learningAPISpring.dto.DTO.ProductFilterDTO;
import com.example.learningAPISpring.entity.Product;
import com.example.learningAPISpring.exception.ResourceNotFoundEX;
import com.example.learningAPISpring.mapper.ProductMapper;
import com.example.learningAPISpring.repository.ProductRepository;
import com.example.learningAPISpring.specification.ProductSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product addProduct(ProductDTO productDto) {
        Product product = productMapper.mapToProductEntity(productDto);
        return productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getAllProducts(UUID categoryId, UUID typeId) {

        Specification<Product> productSpecification= Specification.where(null);

        if(null != categoryId){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if(null != typeId){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryTypeId(typeId));
        }

        List<Product> products = productRepository.findAll(productSpecification);
        return productMapper.getProductDtos(products);
    }


    public Page<Product> getProducts(ProductFilterDTO filter) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name"); // default
        if (filter.getSortBy() != null && filter.getSortDir() != null) {
            sort = filter.getSortDir().equalsIgnoreCase("desc") ?
                    Sort.by(Sort.Direction.DESC, filter.getSortBy()) :
                    Sort.by(Sort.Direction.ASC, filter.getSortBy());
        }

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10,
                sort
        );

        return productRepository.findAll(ProductSpecification.filterProducts(filter), pageable);
    }

    @Override
    public ProductDTO getProductBySlug(String slug) {
        Product product= productRepository.findBySlug(slug);
        if(null == product){
            throw new ResourceNotFoundEX("Product Not Found!");
        }
        ProductDTO productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }

    @Override
    public ProductDTO getProductById(UUID id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEX("Product Not Found!"));
        ProductDTO productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        productDto.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return productDto;
    }

    @Override
    public Product updateProduct(ProductDTO productDto, UUID id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEX("Product Not Found!"));
        productDto.setId(product.getId());
        return productRepository.save(productMapper.mapToProductEntity(productDto));
    }

    @Override
    public Product fetchProductById(UUID id) throws Exception {
        return productRepository.findById(id).orElseThrow(BadRequestException::new);
    }

    @Override
    public void deleteProductById(UUID id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEX("Product Not Found!"));
        productRepository.delete(product);
    }



}
