package com.tester.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tester.entity.TestPlan;
@Repository


public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {
	
	
	@Query("SELECT tp " +
		       "FROM TestPlan tp " +
		       "LEFT JOIN tp.developer dev " +
		       "LEFT JOIN tp.systemModule mod " +
		       "LEFT JOIN tp.tester tester " +
		       "WHERE (:name IS NULL OR tp.name ILIKE %:name%) " +
		       "AND (:observation IS NULL OR tp.observation ILIKE %:observation%) " +
		       "AND (:status IS NULL OR tp.status = :status) " +
		       "AND (:taskStatus IS NULL OR tp.taskStatus = :taskStatus) " +
		       "AND (:jira IS NULL OR tp.jira ILIKE %:jira%) " +
		       "AND (:matriz IS NULL OR tp.matriz ILIKE %:matriz%) " +
		       "AND (:userName IS NULL OR tp.userName ILIKE %:userName%) " +
		       "AND (:callNumber IS NULL OR tp.callNumber ILIKE %:callNumber%) " +
		       "AND (:developerName IS NULL OR dev.name ILIKE %:developerName%) " +
		       "AND (:systemModuleName IS NULL OR mod.name ILIKE %:systemModuleName%) " +
		       "AND (:testerQAName IS NULL OR tester.name ILIKE %:testerQAName%) " +
		       "AND (:dataInicio IS NULL OR tp.data >= :dataInicio) " +
		       "AND (:dataFim IS NULL OR tp.data <= :dataFim) " +
		       "AND (:deliveryDataInicio IS NULL OR tp.deliveryData >= :deliveryDataInicio) " +
		       "AND (:deliveryDataFim IS NULL OR tp.deliveryData <= :deliveryDataFim)"+
		       "ORDER BY tp.data DESC, tester.name ASC ")
		Page<TestPlan> findAllTestPlans(
		    @Param("name") String name,
		    @Param("observation") String observation,
		    @Param("status") String status,
		    @Param("taskStatus") String taskStatus,
		    @Param("jira") String jira,
		    @Param("dataInicio") LocalDate dataInicio,
		    @Param("dataFim") LocalDate dataFim,
		    @Param("deliveryDataInicio") LocalDate deliveryDataInicio,
		    @Param("deliveryDataFim") LocalDate deliveryDataFim,
		    @Param("matriz") String matriz,
		    @Param("userName") String userName,
		    @Param("callNumber") String callNumber,
		    @Param("developerName") String developerName,
		    @Param("systemModuleName") String systemModuleName,
		    @Param("testerQAName") String testerQAName,
		    Pageable pageable);
	
	

	

}
