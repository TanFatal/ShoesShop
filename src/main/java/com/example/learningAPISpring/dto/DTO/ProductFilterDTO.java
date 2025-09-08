package com.example.learningAPISpring.dto.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDTO {
    private String name;
    private UUID categoryId;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy;
    private String sortDir;
    private Integer page;
    private Integer size;
}
