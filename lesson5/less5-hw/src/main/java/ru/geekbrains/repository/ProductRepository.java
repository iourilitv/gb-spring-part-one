package ru.geekbrains.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("from Product p where p.title = :title")
    List<Product> getProductsByTitle(@Param("title") String title);

    @Query("from Product p where p.price between :minVal and :maxVal")
    List<Product> filterProductsByPriceBetween(@Param("minVal") BigDecimal minVal,
                                               @Param("maxVal") BigDecimal maxVal);

    Page<Product> findAllByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    Page<Product> findAllByPriceGreaterThanEqual(BigDecimal min, Pageable pageable);

    Page<Product> findAllByPriceLessThanEqual(BigDecimal max, Pageable pageable);

}
