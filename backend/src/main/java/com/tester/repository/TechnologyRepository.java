package com.tester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tester.entity.Technology;
@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    // Buscar tecnologias ativas
    List<Technology> findByActiveTrue();

    // Buscar tecnologias inativas
    List<Technology> findByActiveFalse();

	boolean existsByName(String name);

}
