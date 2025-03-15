package com.tester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tester.dto.TestCaseDTO;
import com.tester.entity.TestCaseEntity;
@Repository
public interface TestCaseRepository extends JpaRepository<TestCaseEntity,Long>{
	
	  @Query("""
		        SELECT new com.tester.dto.TestCaseDTO(
		            tc.id,
		            tc.scenario,
		            tc.expectedResult,
		            tc.obtainedResult,
		            tc.videoEvidence,
		            tc.status,
		            tc.data,
		            ts.id AS testSuiteId,
		            tp.id AS testPlanId
		        )
		        FROM TestCaseEntity tc
		        JOIN tc.testSuite ts
		        JOIN ts.testPlan tp
		        WHERE tp.id = :testPlanId
		          AND ts.id = :testSuiteId
		    """)
		    List<TestCaseDTO> findTestCasesByTestPlanAndTestSuite(
		        @Param("testPlanId") Long testPlanId,
		        @Param("testSuiteId") Long testSuiteId
		    );

}
