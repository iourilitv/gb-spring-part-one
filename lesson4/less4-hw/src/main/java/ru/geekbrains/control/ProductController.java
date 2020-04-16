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

    //перехватывает запросы и /product и типа http://localhost:8080/app/product?minPrice=&maxPrice=20
    //чтобы этого избежать нужно использовать запрос с параметром @RequestParam(..., required = false)
//    @GetMapping("")
//    public String allProducts(Model model) {
//        model.addAttribute("products", productService.getAllProducts());
//        return "products";
//    }

    //при обработке запроса с пустыми параметрами
    //например - http://localhost:8080/app/product?minPrice=&maxPrice=20
    // Ошибка - "Required int parameter 'minPrice' is not present"
    //Причина - !!!НЕЛЬЗЯ использовать примитивный тип!!! Можно только обертки!
//    @GetMapping()
//    public String allProducts(@RequestParam(value = "minPrice") int minPrice,
//                              @RequestParam(value = "maxPrice") int maxPrice,
//                              Model model) {
//        model.addAttribute("products",
//                productService.getAllProductsByPriceBetween(minPrice, maxPrice));
//        return "products";
//    }

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

//    @GetMapping(path = "?MinPrice={minPrice}&MaxPrice={maxPrice}")
//    public String allProductsBetween(@PathVariable String minPrice,
//                                     @PathVariable String maxPrice, Model model) {
//        int minInt = 0, maxInt = Integer.MAX_VALUE;
//        if(!minPrice.isEmpty()) {
//            minInt = Integer.parseInt(minPrice);
//        }
//        if(!maxPrice.isEmpty()) {
//            maxInt = Integer.parseInt(maxPrice);
//        }
////        int minInt = Integer.parseInt(minPrice);
////        int maxInt = Integer.parseInt(maxPrice);
//        System.out.println("ProductController.allProducts() - minPrice: " + minPrice);
//        System.out.println("ProductController.allProducts() - maxPrice: " + maxPrice);
////        if(minInt < 0) {
////            minInt = 0;
////        }
////        if(maxInt < 0) {
////            maxInt = Integer.MAX_VALUE;
////        }
//        model.addAttribute("products",
//                productService.getAllProductsByPriceBetween(minInt, maxInt));
//        return "products";
//    }

//    @GetMapping
//    public String allPersons(@RequestParam("minPrice") BigDecimal minPrice,
//                             @RequestParam("maxPrice") BigDecimal maxPrice,
//                             Model model) {
//        model.addAttribute("persons", personService.getAllPersons());
//        return "persons";
//    }

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
