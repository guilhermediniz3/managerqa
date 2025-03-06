package com.tester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tester.entity.SystemModule;



@Repository	
public interface SystemModuleRepository extends JpaRepository<SystemModule, Long> {



}
	