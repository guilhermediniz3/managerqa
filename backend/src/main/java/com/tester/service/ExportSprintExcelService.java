package com.tester.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tester.dto.TestPlanByTechnologyDTO;
import com.tester.enuns.Status;
import com.tester.repository.TestPlanRepository;

@Service
public class ExportSprintExcelService {

    private final TestPlanRepository testPlanRepository;

    public ExportSprintExcelService(TestPlanRepository testPlanRepository) {
        this.testPlanRepository = testPlanRepository;
    }

    public byte[] exportToExcel(LocalDate dataInicio, LocalDate dataFim,
                              String tecnologia, String statusStr,
                              String developerName) throws IOException {
        
        Status status = parseStatus(statusStr);
        
        List<TestPlanByTechnologyDTO> data = testPlanRepository.findForExport(
            dataInicio, dataFim, tecnologia, status, developerName);

        return generateExcel(data);
    }

    private Status parseStatus(String statusStr) {
        if (statusStr == null || statusStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Status inválido: '" + statusStr + "'. " +
                "Valores válidos: " + Arrays.toString(Status.values()));
        }
    }

    private byte[] generateExcel(List<TestPlanByTechnologyDTO> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sprints");
            
            // Cabeçalho
            createHeaderRow(workbook, sheet);
            
            // Dados
            fillDataRows(sheet, data);
            
            // Auto ajuste
            autoSizeColumns(sheet);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Sprint", "UL", "Bitrix", "Descrição", "Desenvolvedor", 
                          "Tester", "Módulo", "Tecnologia", "Status"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillDataRows(Sheet sheet, List<TestPlanByTechnologyDTO> data) {
        int rowNum = 1;
        for (TestPlanByTechnologyDTO item : data) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(item.getSprint() != null ? item.getSprint().toString() : "");
            row.createCell(1).setCellValue(item.getUl() != null ? item.getUl() : "");
            row.createCell(2).setCellValue(item.getBitrix() != null ? item.getBitrix() : "");
            row.createCell(3).setCellValue(item.getDescricao() != null ? item.getDescricao() : "");
            row.createCell(4).setCellValue(item.getDesenvolvedor() != null ? item.getDesenvolvedor() : "");
            row.createCell(5).setCellValue(item.getTester() != null ? item.getTester() : "");
            row.createCell(6).setCellValue(item.getModulo() != null ? item.getModulo() : "");
            row.createCell(7).setCellValue(item.getTecnologia() != null ? item.getTecnologia() : "");
            row.createCell(8).setCellValue(item.getStatus() != null ? item.getStatus().toString() : "");
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}