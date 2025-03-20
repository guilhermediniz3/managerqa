package com.tester.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tester.dto.TestCaseDTO;
import com.tester.entity.TestCaseEntity;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Long> {

	@Query("""
		    SELECT new com.tester.dto.TestCaseDTO(
		        tc.id,
		        tc.codeCase,  
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
		    ORDER BY tc.codeCase ASC
		""")
		List<TestCaseDTO> findTestCasesByTestPlanAndTestSuite(@Param("testPlanId") Long testPlanId,
		        @Param("testSuiteId") Long testSuiteId);
	
	@Query(value = "SELECT tc.code_case " +
            "FROM operational.tb_test_case tc " +
            "JOIN operational.tb_test_suite ts ON tc.test_suite_id = ts.id " +
            "WHERE ts.test_plan_id = :testPlanId AND tc.test_suite_id = :testSuiteId " +
            "ORDER BY tc.code_case DESC " +
            "LIMIT 1", nativeQuery = true)
Optional<Long> findLastCodeCaseByTestPlanIdAndTestSuiteId(
 @Param("testPlanId") Long testPlanId,
 @Param("testSuiteId") Long testSuiteId
);
}
