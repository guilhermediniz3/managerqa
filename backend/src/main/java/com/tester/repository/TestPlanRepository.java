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
import com.tester.dto.TestPlanByTechnologyDTO;
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
	
	
	// querys referente a exportacao de pdf e xls
	
	// retorna quem criou a ul


	@Query("SELECT new com.tester.dto.CreatedByDTO(t.created_by) FROM TestPlan t WHERE t.id = :testPlanId")
	CreatedByDTO findCreatedByByTestPlanId(@Param("testPlanId") Long testPlanId);
	
	// ------------------------------------------------------------------------------------------------------------


     
	 // Busca tarefas criadas pelo created_by e data
    @Query("SELECT t FROM TestPlan t WHERE t.created_by = :createdBy AND t.data = :data")
    List<TestPlan> findCriadasByCreatedByAndData(@Param("createdBy") String createdBy, @Param("data") LocalDate data);

    // Busca tarefas pelo nome do tester (tester.name) e data
    @Query("SELECT t FROM TestPlan t JOIN t.tester c WHERE c.name = :testerName AND t.data = :data")
    List<TestPlan> findByTesterNameAndData(@Param("testerName") String testerName, @Param("data") LocalDate data);

    // Busca tarefas pelo nome do tester (tester.name), data e status
    @Query("SELECT t FROM TestPlan t JOIN t.tester c WHERE c.name = :testerName AND t.data = :data AND t.status = :status")
    List<TestPlan> findByTesterNameAndDataAndStatus(@Param("testerName") String testerName, @Param("data") LocalDate data, @Param("status") Status status);

    // Busca todas as tarefas pela data
    @Query("SELECT t FROM TestPlan t WHERE t.data = :data")
    List<TestPlan> findByData(@Param("data") LocalDate data);

    // Conta tarefas pelo nome do tester (tester.name), data e status
    @Query("SELECT COUNT(t) FROM TestPlan t JOIN t.tester c WHERE c.name = :testerName AND t.data = :data AND t.status = :status")
    Long countByTesterNameAndDataAndStatus(@Param("testerName") String testerName, @Param("data") LocalDate data, @Param("status") Status status);

    // Conta tarefas pela data e status
    @Query("SELECT COUNT(t) FROM TestPlan t WHERE t.data = :data AND t.status = :status")
    Long countByDataAndStatus(@Param("data") LocalDate data, @Param("status") Status status);

    // Conta tarefas criadas pelo created_by e data
    @Query("SELECT COUNT(t) FROM TestPlan t WHERE t.created_by = :createdBy AND t.data = :data")
    Long countCriadasByCreatedByAndData(@Param("createdBy") String createdBy, @Param("data") LocalDate data);
    
    
    // querys referente ao dashboard
    
 // Query para contar tarefas CONCLUÍDAS
    @Query("SELECT COUNT(t) FROM TestPlan t " +
           "WHERE t.deliveryData BETWEEN :dataDe AND :dataAte " +
           "AND t.status = 'CONCLUIDA'")
    Long countConcluidas(@Param("dataDe") LocalDate dataDe, @Param("dataAte") LocalDate dataAte);

    // Query para contar tarefas com RETORNO
    @Query("SELECT COUNT(t) FROM TestPlan t " +
           "WHERE t.deliveryData BETWEEN :dataDe AND :dataAte " +
           "AND t.status = 'RETORNO'")
    Long countRetorno(@Param("dataDe") LocalDate dataDe, @Param("dataAte") LocalDate dataAte);

    // Query para contar tarefas com IMPEDIMENTO
    @Query("SELECT COUNT(t) FROM TestPlan t " +
           "WHERE t.deliveryData BETWEEN :dataDe AND :dataAte " +
           "AND t.status = 'IMPEDIMENTO'")
    Long countImpedimento(@Param("dataDe") LocalDate dataDe, @Param("dataAte") LocalDate dataAte);

    // Query para contar tarefas EM PROGRESSO
    @Query("SELECT COUNT(t) FROM TestPlan t " +
           "WHERE t.deliveryData BETWEEN :dataDe AND :dataAte " +
           "AND t.status = 'EM_PROGRESSO'")
    Long countEmProgresso(@Param("dataDe") LocalDate dataDe, @Param("dataAte") LocalDate dataAte);

    // Query para contar registros onde created = TRUE dentro de um intervalo de datas
    @Query("SELECT COUNT(t) FROM TestPlan t " +
           "WHERE t.deliveryData BETWEEN :dataDe AND :dataAte " +
           "AND t.created = TRUE")
    Long countCreatedTrueByDeliveryDataBetween(
            @Param("dataDe") LocalDate dataDe,
            @Param("dataAte") LocalDate dataAte);

// dashboard exportar excel e pdf ---------------------------------------------------------------------------------------------------------------
    

    @Query("""
            SELECT NEW com.tester.dto.TestPlanByTechnologyDTO(
                tp.deliveryData,
                tp.jira,
                tp.callNumber,
                tp.name,
                dev.name,
                tester.name,
                sm.name,
                tech.name,
                tp.status
            )
            FROM TestPlan tp
            LEFT JOIN tp.developer dev
            LEFT JOIN dev.technologies tech
            LEFT JOIN tp.systemModule sm
            LEFT JOIN tp.tester tester
            WHERE (COALESCE(:dataInicio, null) IS NULL OR tp.deliveryData >= :dataInicio)
              AND (COALESCE(:dataFim, null) IS NULL OR tp.deliveryData <= :dataFim)
              AND (COALESCE(:tecnologia, null) IS NULL OR tech.name = :tecnologia)
              AND (COALESCE(:status, null) IS NULL OR tp.status = :status)
              AND (COALESCE(:developerName, null) IS NULL OR dev.name LIKE %:developerName%)
            ORDER BY tp.status, tp.deliveryData DESC, dev.name
        """)
        List<TestPlanByTechnologyDTO> findForExport(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("tecnologia") String tecnologia,
            @Param("status") Status status,
            @Param("developerName") String developerName);
}