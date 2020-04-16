package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("from Product p where p.title = :title")
    List<Product> getProductsByTitle(@Param("title") String title);

    @Query("from Product p where p.price between :minVal and :maxVal")
    List<Product> filterProductsByPriceBetween(@Param("minVal") int minVal,
                                            @Param("maxVal") int maxVal);
}
