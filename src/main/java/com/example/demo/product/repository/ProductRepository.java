package com.example.demo.product.repository;

import com.example.demo.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAll() ;
    @Query("select p from Product p where p.name like %:name%" )
    List<Product> getSuggestionProduct(@Param("name") String name);
    Product findById(UUID id);
}
