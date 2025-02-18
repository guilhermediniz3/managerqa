package com.tester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tester.entity.TestPlan;
@Repository
public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
	

}
