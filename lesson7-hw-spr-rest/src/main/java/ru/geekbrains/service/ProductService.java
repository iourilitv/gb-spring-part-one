package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;

        //TODO for test only
        //наполняем БД 20-ю продуктами
        fillDBWithTestData(20);
    }

    //TODO for test only
    //наполняем БД заданным количеством продуктов
    private void fillDBWithTestData(int numbers) {
        List<Product> productList = new ArrayList<>();
        int i = 1;
        while(i <= numbers) {
            productList.add(new Product());
            productList.get(i - 1).setTitle("product_test_" + i);
            int i1 = i * 10 + i;
            productList.get(i - 1).setPrice(new BigDecimal(i1));
            insert(productList.get(i - 1));
            i++;
        }
    }

    @Transactional
    public void insert(Product product) {
        //если в БД нет дубликата, иначе - исключение и не создает бин,
        //т.к. поле title у Product уникально
        if (productRepository.getProductsByTitle(product.getTitle()).isEmpty()){
            productRepository.save(product);
        }
    }

    @Transactional
    public void update(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteById(long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Product> findAllByAgeBetween(
            Optional<BigDecimal> min, Optional<BigDecimal> max, Pageable pageable) {
        if (min.isPresent() && max.isPresent()) {
            return productRepository.findAllByPriceBetween(min.get(), max.get(), pageable);
        }
        if (min.isPresent()) {
            return productRepository.findAllByPriceGreaterThanEqual(min.get(), pageable);
        }
        if (max.isPresent()) {
            return productRepository.findAllByPriceLessThanEqual(max.get(), pageable);
        }
        return productRepository.findAll(pageable);
    }

}
