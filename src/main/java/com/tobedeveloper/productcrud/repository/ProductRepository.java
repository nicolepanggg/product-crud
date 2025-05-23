package com.tobedeveloper.productcrud.repository;

import com.tobedeveloper.productcrud.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name); // Custom query method: search by product name
    Page<Product> findAll(Pageable pageable); // Page
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
