package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderService extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
