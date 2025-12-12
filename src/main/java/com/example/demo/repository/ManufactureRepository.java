package com.example.demo.repository;


import com.example.demo.domain.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufactureRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByName(String name);

}
