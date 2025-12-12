package com.example.demo.repository;

import com.example.demo.domain.Category;
import com.example.demo.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContryRepository extends JpaRepository<Country, Long> {
    // 부모 ID로 하위 카테고리 목록을 조회 (parent_id = ?)
   // List<Category> findByParent_Id(Long parentId);
}
