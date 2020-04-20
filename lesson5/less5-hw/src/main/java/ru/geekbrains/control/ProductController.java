package ru.geekbrains.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.entity.Product;
import ru.geekbrains.service.ProductService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    //@RequestParam(value = "maxPrice", required = false) - здесь работает только на
//    // "/product" - http://localhost:8080/app/product!
//    @GetMapping
//    public String allProductsBetween(
//            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
//            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
//            Model model) {
//        //проверка, чтобы обработать запрос с пустыми параметрами
//        //например - http://localhost:8080/app/product?minPrice=&maxPrice=20
//        if(minPrice == null) {
//            minPrice = BigDecimal.ZERO;
//        }
//        if(maxPrice == null) {
//            maxPrice = BigDecimal.valueOf(Long.MAX_VALUE);
//        }
//        model.addAttribute("products",
//                productService.getAllProductsByPriceBetween(minPrice, maxPrice));
//        return "products";
//    }
    @GetMapping
    public String allProducts(@RequestParam(value = "minPrice") Optional<BigDecimal> minPrice,
                             @RequestParam(value = "maxPrice") Optional<BigDecimal> maxPrice,
                             @RequestParam(value = "page") Optional<Integer> page,
                             @RequestParam(value = "limit") Optional<Integer> limit,
                             Model model) {
        model.addAttribute("activePage", "Products");
        model.addAttribute("productPage", productService.findAllByAgeBetween(
                minPrice, maxPrice,
                PageRequest.of(page.orElse(1) - 1, limit.orElse(5))
        ));
        model.addAttribute("minPrice", minPrice.orElse(null));
        model.addAttribute("maxPrice", maxPrice.orElse(null));
        return "products";
    }

//    @GetMapping("/form")
//    public String formProduct(Model model) {
//        model.addAttribute("product", new Product());
//        return "product_form";
//    }
    @GetMapping("/form")
    public String formProduct(@RequestParam(value = "id") Optional<Long> productId,  Model model) {
        model.addAttribute("activePage", "ProductForm");
        if(productId.isPresent()) {
            model.addAttribute("product", productService.findById(productId.get()));
        } else {
            model.addAttribute("product", new Product());
        }
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
