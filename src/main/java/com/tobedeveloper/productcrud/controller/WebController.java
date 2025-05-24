package com.tobedeveloper.productcrud.controller;

import com.tobedeveloper.productcrud.entity.Product;
import com.tobedeveloper.productcrud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller // Used to render Thymeleaf templates
public class WebController {

    @Autowired
    private ProductService productService;

    // Home page: Display product list (pagination)
    @GetMapping("/")
    public String showProductList(  // Handle paging and display product lists
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "product-list";
    }

    // Price range filter
    @GetMapping("/price-range")
    public String showProductsByPriceRange(
        @RequestParam(defaultValue = "0") double minPrice, // Extract the value named minPrice from the URL query parameter (e.g. /price-range?minPrice=10.0)
        @RequestParam(defaultValue = "1000000") double maxPrice,
        Model model) {
            List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
            model.addAttribute("products", products); //model. is to pass data from the controller to the view so that it can be displayed or used in the front-end page
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);
        return "product-list";
    }

    // Show new product form
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";  // Specifies the name of the Thymeleaf template to render
    }

    // Handling new products
    @PostMapping("/add")
    public String addProduct(Product product) {
        productService.createProduct(product);
        return "redirect:/";
    }

    // Display the edit product form
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-form";
    }

    // Handle editing products
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

}
