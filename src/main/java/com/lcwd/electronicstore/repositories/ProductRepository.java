package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.payloads.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByTitleContaining(Pageable pageable,String title);

    Page<Product> findByLiveTrue(Pageable pageable);
}
