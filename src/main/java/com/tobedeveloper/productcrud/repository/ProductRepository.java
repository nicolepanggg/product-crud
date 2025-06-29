package com.tobedeveloper.productcrud.repository;

import com.tobedeveloper.productcrud.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //@Query("SELECT p FROM Product p ORDER BY p.id DESC")
    Product findByName(String name); // Custom query method: search by product name
    Page<Product> findAll(Pageable pageable); // Page
    Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
