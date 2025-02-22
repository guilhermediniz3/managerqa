package com.tester.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tester.dto.TestPlanDTO;
import com.tester.entity.TestPlan;
@Repository
public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
	
	 @Query("SELECT NEW com.tester.dto.TestPlanDTO(" +
	           "tp.id, tp.name, tp.observation, tp.status, tp.taskStatus, tp.jira, tp.data, tp.deliveryData, " +
	           "tp.matriz, tp.userName, tp.callNumber, tp.developer.id, tp.systemModule.id, tp.tester.id, " +
	           "NULL, NULL) " + // password e testeSuiteId não estão sendo usados aqui
	           "FROM TestPlan tp " +
	           "JOIN tp.tester t " +
	           "JOIN tp.developer d " +
	           "JOIN tp.systemModule sm")
	    Page<TestPlanDTO> findAllTestPlanDetails(Pageable pageable);

	    @Query("SELECT NEW com.tester.dto.TestPlanDTO(" +
	           "tp.id, tp.name, tp.observation, tp.status, tp.taskStatus, tp.jira, tp.data, tp.deliveryData, " +
	           "tp.matriz, tp.userName, tp.callNumber, tp.developer.id, tp.systemModule.id, tp.tester.id, " +
	           "NULL, NULL) " + // password e testeSuiteId não estão sendo usados aqui
	           "FROM TestPlan tp " +
	           "JOIN tp.tester t " +
	           "JOIN tp.developer d " +
	           "JOIN tp.systemModule sm " +
	           "WHERE (:testerName IS NULL OR t.name = :testerName) " +
	           "   OR (:jira IS NULL OR tp.jira = :jira) " +
	           "   OR (:callNumber IS NULL OR tp.callNumber = :callNumber) " +
	           "   OR (:status IS NULL OR tp.status = :status) " +
	           "   OR (:developerName IS NULL OR d.name = :developerName) " +
	           "   OR (:moduleName IS NULL OR sm.name = :moduleName)")
	    Page<TestPlanDTO> findAllTestPlanDetailsWithOrFilters(
	            @Param("testerName") String testerName,
	            @Param("jira") String jira,
	            @Param("callNumber") String callNumber,
	            @Param("status") String status,
	            @Param("developerName") String developerName,
	            @Param("moduleName") String moduleName,
	            Pageable pageable);

}
