package com.example.demo.repository;

import com.example.demo.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);


    /**
     * 부모 카테고리 ID(parentId)를 기준으로 해당 ID를 parent_id로 갖는
     * 모든 하위(자식) Category 목록을 조회합니다.
     * * JPA는 Category 엔티티 내의 'parent' 필드와 그 ID를 연결하여 쿼리를 생성합니다.
     * (SELECT * FROM category WHERE parent_id = :parentId)
     */
    List<Category> findByParent_Id(Long parentId);
}
