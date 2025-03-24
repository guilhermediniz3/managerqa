package com.tester.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tester.entity.TestPlan;
import com.tester.enuns.Status;

public class TestPlanByTechnologyDTO {
	@JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate sprint;
    private String ul;
    private String bitrix;
    private String descricao;
    private String desenvolvedor;
    private String tester;
    private String modulo;
    private String tecnologia;
    private Status status;
    
    public TestPlanByTechnologyDTO() {}

    // Construtor via composição
    public TestPlanByTechnologyDTO(TestPlan testPlan, String tecnologia) {
        this.sprint = testPlan.getDeliveryData();
        this.ul = testPlan.getJira();
        this.bitrix = testPlan.getCallNumber();
        this.descricao = testPlan.getName();
        this.desenvolvedor = testPlan.getDeveloper() != null ? testPlan.getDeveloper().getName() : null;
        this.tester = testPlan.getTester() != null ? testPlan.getTester().getName() : null;
        this.modulo = testPlan.getSystemModule() != null ? testPlan.getSystemModule().getName() : null;
        this.tecnologia = tecnologia;
        this.status = testPlan.getStatus();
    }
    

    // Construtor para JPA (MANTIDO IGUAL)
    public TestPlanByTechnologyDTO(
            LocalDate sprint,
            String ul,
            String bitrix,
            String descricao,
            String desenvolvedor,
            String tester,
            String modulo,
            String tecnologia,
            Status status) {
        this.sprint = sprint;
        this.ul = ul;
        this.bitrix = bitrix;
        this.descricao = descricao;
        this.desenvolvedor = desenvolvedor;
        this.tester = tester;
        this.modulo = modulo;
        this.tecnologia = tecnologia;
        this.status = status;
    }

    // Getters e Setters
    public LocalDate getSprint() {
        return sprint;
    }

    public void setSprint(LocalDate sprint) {
        this.sprint = sprint;
    }

    public String getUl() {
        return ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    public String getBitrix() {
        return bitrix;
    }

    public void setBitrix(String bitrix) {
        this.bitrix = bitrix;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDesenvolvedor() {
        return desenvolvedor;
    }

    public void setDesenvolvedor(String desenvolvedor) {
        this.desenvolvedor = desenvolvedor;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}