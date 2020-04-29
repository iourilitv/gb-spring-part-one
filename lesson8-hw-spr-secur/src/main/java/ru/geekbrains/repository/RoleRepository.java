package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
