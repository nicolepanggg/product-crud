package com.tobedeveloper.productcrud.controller;

import com.tobedeveloper.productcrud.entity.Product;
import com.tobedeveloper.productcrud.entity.User;
import com.tobedeveloper.productcrud.service.ProductService;
import com.tobedeveloper.productcrud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller // Used to render Thymeleaf templates
public class WebController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Home page: Display product list (pagination)
    @GetMapping("/")
    public String showProductList(  // Handle paging and display product lists
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Model model) {

        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size, Sort.by("id").descending()));
        model.addAttribute("pageTitle", "Product list");
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        //breadcrubs
        model.addAttribute("breadcrumbs", new String[]{"Home", "Product List"});
        model.addAttribute("currentPath", "/");
        return "product-list";
    }

    // Price range filter
    @GetMapping("/price-range")
    public String showProductsByPriceRange(
            @RequestParam(defaultValue = "0") double minPrice, // Extract the value named minPrice from the URL query parameter (e.g. /price-range?minPrice=10.0)
            @RequestParam(defaultValue = "1000000") double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        // Create a Pageable object, using id descending order consistent with showProductList
        //Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending()); //PageRequest internal function
        // Get paging data from the service layer
        Page<Product> productPage = productService.getProductsByPriceRange(minPrice, maxPrice, PageRequest.of(page, size, Sort.by("id").descending()));
        //List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        //model. is to pass data from the controller to the view so that it can be displayed or used in the front-end page
        //model.addAttribute("products", products);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        model.addAttribute("pageSize", size);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        //breadcrubs
        model.addAttribute("breadcrumbs", new String[]{"Home", "Product List", "Price range filter"});
        model.addAttribute("currentPath", "/price-range");
        return "product-list";
    }

    // Show new product form
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "New Products");
        //breadcrubs
        model.addAttribute("breadcrumbs", new String[]{"Home","New Products"});
        model.addAttribute("currentPath", "/add");
        return "product-form";  // Specifies the name of the Thymeleaf template to render
    }

    // Process form submission and go to confirmation page
    //@ModelAttribute("product") tells Spring to automatically bind the form submission data to the Product object
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, Model model) {
        //productService.createProduct(product);
        model.addAttribute("product", product);
        //breadcrubs
        model.addAttribute("breadcrumbs", new String[]{"Home","confirm-product"});
        model.addAttribute("currentPath", "/confirm-product");
        return "confirm-product";
    }

    // Handle submission of confirmation page
    @PostMapping("/confirm-product")
    public String confirmProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute("product") Product product,
            RedirectAttributes redirectAttributes) {
        // Save products to database
        productService.createProduct(product);
        //Display Message: Use RedirectAttributes to pass a one-time success message
        redirectAttributes.addFlashAttribute("message", "Product successfully added！");
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("size", size);
        return "redirect:/";
    }

    //Process the cancellation operation and return to the new page
    @PostMapping("/cancel-product")
    public String cancelProduct() {
        return "redirect:/add";
    }

    // Display the edit product form
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Update information");

        //breadcrubs
        model.addAttribute("breadcrumbs", new String[]{"Home","edit product"});
        model.addAttribute("currentPath", "/edit/{id}");

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

    //Register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Register");
        return "register";
    }

    //@Valid - Enable authentication for the User object
    //@ModelAttribute - Bind form data to User
    //Stores the result of form validation (error message), must be placed immediately after the @Valid parameter

    //Register information
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        model.addAttribute("pageTitle", "Register");
        if(result.hasErrors()) { //Check for validation errors (for example, fields of the User object do not conform to @NotNull, @Email, etc. constraints)
            model.addAttribute("user", user);
            return "register";
        }
        try {
            userService.registerUser(user);
            return "redirect:/login?registered";
        }catch (Exception e) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }
    }

    // Login page
    //Gets a query parameter named error from the request (e.g. /login?error) to indicate a login failure.
    // required = false means the parameter is optional.


    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout, //required = false means this parameter is optional
                                @RequestParam(value = "registered", required = false) String registered) {
        model.addAttribute("pageTitle", "Login");

        if (error != null) {  //Indicates login failure
            model.addAttribute("error", "Invalid email or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have successfully logged out");
        }
        if (registered != null) {
            model.addAttribute("message", "Registration successful, please login");
        }
        return "login";
    }

    // No permission page
    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access-denied";
    }
}



