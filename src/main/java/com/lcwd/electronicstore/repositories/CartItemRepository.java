package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
