package com.tester.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tester.dto.TestSuiteDTO;
import com.tester.entity.TestSuite;

@Repository
public interface TestSuiteRepository extends JpaRepository<TestSuite,Long> {
	

    @Query(value = "SELECT tts.* FROM operational.tb_test_suite tts " +
                   "WHERE tts.test_plan_id = :testPlanId " +
                   "ORDER BY tts.code_suite DESC " +
                   "LIMIT 1", nativeQuery = true)
    Optional<TestSuite> findLastTestSuiteByTestPlanId(@Param("testPlanId") Long testPlanId);
    
    
    
    
    @Query("SELECT new com.tester.dto.TestSuiteDTO(ts.id, ts.codeSuite, ts.status, ts.data, ts.testPlan.id) " +
    	       "FROM TestSuite ts " +
    	       "WHERE ts.testPlan.id = :testPlanId " +
    	       "ORDER BY ts.codeSuite ASC")
    	List<TestSuiteDTO> findAllTestSuitesByTestPlanId(@Param("testPlanId") Long testPlanId);
}
	