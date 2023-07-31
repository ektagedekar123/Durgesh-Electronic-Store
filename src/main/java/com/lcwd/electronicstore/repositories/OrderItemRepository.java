package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems, Integer> {


}
