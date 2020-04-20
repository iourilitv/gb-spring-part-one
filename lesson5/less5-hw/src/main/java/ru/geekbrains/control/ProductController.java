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

    @GetMapping
    public String allProducts(@RequestParam(value = "minPrice") Optional<BigDecimal> minPrice,
                             @RequestParam(value = "maxPrice") Optional<BigDecimal> maxPrice,
                             @RequestParam(value = "page") Optional<Integer> page,
                             @RequestParam(value = "limit") Optional<Integer> limit,
                             Model model) {
        //добавляем атрибут активной страницы со значением страницы каталога
        //см. в header.html
        model.addAttribute("activePage", "Products");
        //устанавливаем атрибут для пагинации см. в pagination.html
        model.addAttribute("productPage", productService.findAllByAgeBetween(
                minPrice, maxPrice,
                PageRequest.of(page.orElse(1) - 1, limit.orElse(5))
        ));
        model.addAttribute("minPrice", minPrice.orElse(null));
        model.addAttribute("maxPrice", maxPrice.orElse(null));
        return "products";
    }

    @GetMapping("/form")
    public String formProduct(@RequestParam(value = "productId") Optional<Long> productId,  Model model) {
        //добавляем атрибут активной страницы со значением страницы каталога
        //см. в header.html
        model.addAttribute("activePage", "ProductForm");
        if(productId.isPresent()) {
            model.addAttribute("product", productService.findById(productId.get()));
        } else {
            model.addAttribute("product", new Product());
        }
        model.addAttribute("productId", productId.orElse(null));
        return "product_form";
    }

//    @PostMapping("/form")
//    public String newProduct(@Valid Product product, BindingResult bindingResult) {
//        if(bindingResult.hasErrors()) {
//            return "product_form";
//        }
//        productService.insert(product);
//        return "redirect:/product";
//    }
    @PostMapping("/form")
    public String newProduct(@Valid Product product, BindingResult bindingResult,
                             @RequestParam(value = "productId") Optional<Long> productId) {
        if(bindingResult.hasErrors()) {
            return "product_form";
        }
        //FIXME Не работает!!! Добавляем новый товар каждый раз.
//        if(product.getId() == null) {
//            productService.insert(product);
//        } else {
//            productService.update(product);
//        }
        //FIXME Не работает!!! Добавляем новый товар каждый раз.
        if(productId.isPresent()) {
            productService.update(product);
        } else {
            productService.insert(product);
        }
        return "redirect:/product";
    }

}
