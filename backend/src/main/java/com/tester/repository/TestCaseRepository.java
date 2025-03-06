package com.tester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tester.entity.TestCaseEntity;
@Repository
public interface TestCaseRepository extends JpaRepository<TestCaseEntity,Long>{

}
