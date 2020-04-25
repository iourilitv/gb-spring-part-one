package ru.geekbrains.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.entity.Product;
import ru.geekbrains.exception.NotFoundException;
import ru.geekbrains.exception.ProductErrorResponse;
import ru.geekbrains.service.ProductService;

import java.util.List;

@RequestMapping("/api/v1/product")
@RestController
public class ProductResource {

    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    //указываем формат данных в представлении REST ресурса
    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)//produces = "application/json"
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @GetMapping(path = "/{id}/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product findById(@PathVariable("id") long id) {
        return productService.findById(id)
                .orElseThrow(() -> new NotFoundException("The page is not found!"));//по умолчанию вернет код ошибки 500 - ошибка на сервере
    }

    @PostMapping
    public void createProduct(@RequestBody Product product) {
        //если в запросе указан id - такой объект уже есть в БД
        if (product.getId() != null) {
            throw new IllegalArgumentException("A Product with this id already exists!");
        }
        productService.insert(product);
    }

    @PutMapping
    public void updateProduct(@RequestBody Product product) {
        productService.update(product);
    }

    @DeleteMapping(path = "/{id}/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") long id) {
        productService.deleteById(id);
    }

    //метод перехватывает ошибку 500 и возвращает 404
    @ExceptionHandler
    public ResponseEntity<ProductErrorResponse> notFoundExceptionHandler(NotFoundException exception) {
        ProductErrorResponse response = new ProductErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //метод перехватывает ошибку 500 и возвращает 400 - Bad request
    @ExceptionHandler
    public ResponseEntity<ProductErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException exception) {
        ProductErrorResponse response = new ProductErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
