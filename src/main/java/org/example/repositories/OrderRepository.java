package org.example.repositories;

import java.util.List;
import org.example.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserEmail(String email);
}
