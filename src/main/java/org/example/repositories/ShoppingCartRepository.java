package org.example.repositories;

import java.util.Optional;
import org.example.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);

    Optional<ShoppingCart> findByUserEmail(String email);
}
