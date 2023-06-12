package com.lcwd.electronicstore.repositories;

import com.lcwd.electronicstore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String>{

    List<Category> findByTitleContaining(String keywords);
}
