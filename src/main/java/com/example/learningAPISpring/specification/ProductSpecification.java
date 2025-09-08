package com.example.learningAPISpring.specification;
import com.example.learningAPISpring.dto.DTO.ProductFilterDTO;
import com.example.learningAPISpring.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class ProductSpecification {

    public static Specification<Product> hasCategoryId(UUID categorId){
        return  (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"),categorId);
    }

    public static Specification<Product> hasCategoryTypeId(UUID typeId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoryType").get("id"),typeId);
    }


    public static Specification<Product> filterProducts(ProductFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
