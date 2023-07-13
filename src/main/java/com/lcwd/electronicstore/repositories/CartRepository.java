package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
