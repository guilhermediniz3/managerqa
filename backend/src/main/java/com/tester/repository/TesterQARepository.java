package com.tester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tester.entity.TesterQA;
@Repository
public interface TesterQARepository extends JpaRepository<TesterQA,Long>{

}
