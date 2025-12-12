package com.example.demo.repository;

import com.example.demo.domain.BaseSpec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseSpecRepository extends JpaRepository<BaseSpec, Long> {

}
