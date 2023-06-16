package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.payloads.PageableResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByTitleContaining(String title);

    List<Product> findByLiveTrue();
}
