package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
