package com.tobedeveloper.productcrud.controller;

import com.tobedeveloper.productcrud.entity.Product;
import com.tobedeveloper.productcrud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

//REST API endpoints, handling HTTP requests.
@RestController
@RequestMapping(path = "/api/products")

public class ProductController {
    // The main function is to automatically inject the beans in the Spring
    // container into the fields, methods or constructors marked with this
    // annotation, thereby simplifying the creation and management of objects.
    @Autowired
    private ProductService productService;

    // Create Product
    // @RequestBody, The main function is to deserialize the data in the HTTP request body into Java objects.
    @PostMapping //Specialized for handling HTTP POST requests
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Get all product
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // "@PathVariable" Allows you to extract dynamic values ​​from the requested URL
    // "ResponseEntity" It allows developers to flexibly control various parts of the HTTP response and is
    // particularly suitable for the development of RESTful APIs.
    // orElseGet Used to handle situations where Optional objects may be empty

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) { //Extract the value of {id} from the URL path
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get product by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Get page
    @GetMapping("/page")
    public Page<Product> getProductsByPage(@RequestParam int page, @RequestParam int size) {
        return productService.getAllProducts(PageRequest.of(page, size));
    }

    //Get price range
    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }
}
