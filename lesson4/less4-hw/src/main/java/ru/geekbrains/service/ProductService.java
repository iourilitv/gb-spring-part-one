package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repository.ProductRepository;

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
            productList.get(i - 1).setPrice(i1);
            insert(productList.get(i - 1));
            i++;
        }
    }

    //выбрасывает исключение и не создает бин при попытке добавить дубликат,
    //т.к. поле title у Product уникально
//    @Transactional
//    public void insert(Product product) {
//        productRepository.save(product);
//    }
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
        //FIXME нельзя использовать save для изменения данных
        productRepository.save(product);
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
    public List<Product> getAllProductsByPriceBetween(int minVal, int maxVal) {
        return productRepository.filterProductsByPriceBetween(minVal, maxVal);
    }

}
