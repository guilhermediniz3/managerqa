package com.tester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tester.entity.TestSuite;

@Repository
public interface TestSuiteRepository extends JpaRepository<TestSuite,Long> {
	
	
	

}
