package com.tester.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tester.dto.CreatedByDTO;

import com.tester.entity.TestPlan;
import com.tester.enuns.Status;

@Repository

public interface TestPlanRepository extends JpaRepository<TestPlan, Long> {

	@Query("SELECT tp " + "FROM TestPlan tp " + "LEFT JOIN tp.developer dev " + "LEFT JOIN tp.systemModule mod "
			+ "LEFT JOIN tp.tester tester " + "WHERE (:name IS NULL OR tp.name ILIKE %:name%) "
			+ "OR (:observation IS NULL OR tp.observation ILIKE %:observation%) "
			+ "OR (:status IS NULL OR tp.status = :status) "
			+ "OR (:taskStatus IS NULL OR tp.taskStatus = :taskStatus) "
			+ "OR (:jira IS NULL OR tp.jira ILIKE %:jira%) " + "OR (:matriz IS NULL OR tp.matriz ILIKE %:matriz%) "
			+ "OR (:userName IS NULL OR tp.userName ILIKE %:userName%) "
			+ "OR (:callNumber IS NULL OR tp.callNumber ILIKE %:callNumber%) "
			+ "OR (:developerName IS NULL OR dev.name ILIKE %:developerName%) "
			+ "OR (:systemModuleName IS NULL OR mod.name ILIKE %:systemModuleName%) "
			+ "OR (:testerQAName IS NULL OR tester.name ILIKE %:testerQAName%) "
			+ "AND (:dataInicio IS NULL OR tp.data >= :dataInicio) " + "AND (:dataFim IS NULL OR tp.data <= :dataFim) "
			+ "AND (:deliveryDataInicio IS NULL OR tp.deliveryData >= :deliveryDataInicio) "
			+ "AND (:deliveryDataFim IS NULL OR tp.deliveryData <= :deliveryDataFim) "
			+ "ORDER BY tp.data DESC, tester.name ASC")
	Page<TestPlan> findAllTestPlans(@Param("name") String name, @Param("observation") String observation,
			@Param("status") String status, @Param("taskStatus") String taskStatus, @Param("jira") String jira,
			@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim,
			@Param("deliveryDataInicio") LocalDate deliveryDataInicio,
			@Param("deliveryDataFim") LocalDate deliveryDataFim, @Param("matriz") String matriz,
			@Param("userName") String userName, @Param("callNumber") String callNumber,
			@Param("developerName") String developerName, @Param("systemModuleName") String systemModuleName,
			@Param("testerQAName") String testerQAName, Pageable pageable);

	// TESTER SE NAO DER CERTO EXCLUIR
	@Query("SELECT tp " + "FROM TestPlan tp " + "LEFT JOIN FETCH tp.tester tester "
			+ "LEFT JOIN FETCH tp.developer developer " + "LEFT JOIN FETCH tp.systemModule systemModule "
			+ "LEFT JOIN FETCH tp.testeSuite testSuite " + "WHERE (:searchValue IS NULL OR "
			+ "       LOWER(tp.callNumber) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
			+ "       LOWER(tp.jira) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
			+ "       LOWER(tp.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
			+ "       LOWER(tp.status) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
			+ "       LOWER(tester.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
			+ "       LOWER(developer.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
			+ "       LOWER(systemModule.name) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " + "ORDER BY tp.data DESC")
	Page<TestPlan> searchTestPlans(@Param("searchValue") String searchValue, Pageable pageable);
	
	// retorna quem criou a ul

	@Query("SELECT new com.tester.dto.CreatedByDTO(t.created_by) FROM TestPlan t WHERE t.id = :testPlanId")
	CreatedByDTO findCreatedByByTestPlanId(@Param("testPlanId") Long testPlanId);

	// relatorios

	@Query("SELECT t FROM TestPlan t WHERE t.created_by = :testerName AND t.data = :data")
	List<TestPlan> findByCreatedByAndData(@Param("testerName") String testerName, @Param("data") LocalDate data);

	@Query("SELECT t FROM TestPlan t WHERE t.data = :data")
	List<TestPlan> findByData(@Param("data") LocalDate data);

	@Query("SELECT t FROM TestPlan t WHERE t.data = :data AND t.status = :status")
	List<TestPlan> findByDataAndStatus(@Param("data") LocalDate data, @Param("status") Status status);

	@Query("SELECT t FROM TestPlan t WHERE t.created_by = :testerName AND t.data = :data AND t.status = :status")
	List<TestPlan> findByCreatedByAndDataAndStatus(@Param("testerName") String testerName,
			@Param("data") LocalDate data, @Param("status") Status status);

	@Query("SELECT COUNT(t) FROM TestPlan t WHERE t.created_by = :testerName AND t.data = :data AND t.status = :status")
	Long countByCreatedByAndDataAndStatus(@Param("testerName") String testerName, @Param("data") LocalDate data,
			@Param("status") Status status);

	@Query("SELECT COUNT(t) FROM TestPlan t WHERE t.data = :data AND t.status = :status")
	Long countByDataAndStatus(@Param("data") LocalDate data, @Param("status") Status status);

	@Query("SELECT COUNT(t) FROM TestPlan t WHERE t.created_by = :testerName AND t.data = :data")
	Long countByCreatedByAndData(@Param("testerName") String testerName, @Param("data") LocalDate data);
}
