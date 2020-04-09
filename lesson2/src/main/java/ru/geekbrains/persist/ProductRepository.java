package ru.geekbrains.persist;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ProductRepository {

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    private Map<Integer, Product> products = Collections.synchronizedMap(new HashMap<>());

//    public ProductRepository() {
//        insert(new Product("good1", "120"));
//        insert(new Product("good2", "10"));
//        insert(new Product("good3", "1005"));
//    }
    public ProductRepository() {
        insert(new Product("good1", 120));
        insert(new Product("good2", 10));
        insert(new Product("good3", 1005));
    }

    public void insert(Product product) {
        int id = atomicInteger.incrementAndGet();
        product.setId(id);
        products.put(id, product);
    }

    public Map<Integer, Product> getAllProducts() {
        return Collections.unmodifiableMap(products);
    }
//    public Collection<Product> getAllProducts() {
//        return Collections.unmodifiableMap(products).values();
//    }

    public Product getProductById(int id) {
        return products.get(id);
    }
}
