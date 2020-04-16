package ru.geekbrains.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.entity.Product;
import ru.geekbrains.service.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //@RequestParam(value = "maxPrice", required = false) - здесь работает только на
    // "/product" - http://localhost:8080/app/product!
    @GetMapping
    public String allProductsBetween(
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            Model model) {
        //проверка, чтобы обработать запрос с пустыми параметрами
        //например - http://localhost:8080/app/product?minPrice=&maxPrice=20
        if(minPrice == null) {
            minPrice = 0;
        }
        if(maxPrice == null) {
            maxPrice = Integer.MAX_VALUE;
        }
        model.addAttribute("products",
                productService.getAllProductsByPriceBetween(minPrice, maxPrice));
        return "products";
    }

    @GetMapping("/form")
    public String formProduct(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping("/form")
    public String newProduct(@Valid Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "product_form";
        }
        productService.insert(product);
        return "redirect:/product";
    }

}
