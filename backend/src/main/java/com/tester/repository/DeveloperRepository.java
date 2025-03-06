package com.tester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tester.entity.Developer;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

    // Buscar desenvolvedores ativos
    List<Developer> findByActiveTrue();

    // Buscar desenvolvedores inativos
    List<Developer> findByActiveFalse();

}