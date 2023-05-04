package com.example.demo.header;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeaderRepository extends JpaRepository<Header, Integer> {
    @Query("select e from Header e")
    public List<Header> getHeaders();
}
