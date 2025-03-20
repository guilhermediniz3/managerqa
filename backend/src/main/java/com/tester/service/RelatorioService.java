package com.tester.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tester.entity.TestPlan;
import com.tester.enuns.Status;
import com.tester.repository.TestPlanRepository;

@Service
public class RelatorioService {

    @Autowired
    private TestPlanRepository repository;

    /**
     * Gera um relatório Excel com base na data e no nome do tester.
     *
     * @param data       Data para filtrar os registros
     * @param testerName Nome do tester (opcional)
     * @return Byte array contendo o arquivo Excel
     * @throws IOException Se ocorrer um erro ao gerar o arquivo
     */
    public byte[] gerarRelatorioExcel(LocalDate data, String testerName) throws IOException {
        // Consulta os dados no banco
        List<TestPlan> dados;
        if (testerName != null) {
            dados = repository.findByCreatedByAndData(testerName, data);
        } else {
            dados = repository.findByData(data);
        }

        // Verifica se há dados
        if (dados == null || dados.isEmpty()) {
            throw new IOException("Nenhum dado encontrado para a data especificada.");
        }

        // Cria o workbook e a planilha
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Relatório");

            // Define os cabeçalhos da tabela
            String[] headers = {"Tester", "Data", "Jira", "Call Number", "Status"};

            // Cria o estilo para cabeçalhos
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Agrupa os dados por tester (created_by), ignorando registros com created_by nulo
            Map<String, List<TestPlan>> dadosAgrupadosPorTester = dados.stream()
                    .filter(item -> item.getCreated_by() != null) // Filtra registros com created_by nulo
                    .collect(Collectors.groupingBy(TestPlan::getCreated_by));

            int rowNum = 0; // Inicia na linha 0

            // Itera sobre cada tester e seus registros
            for (Map.Entry<String, List<TestPlan>> entry : dadosAgrupadosPorTester.entrySet()) {
                String testerAtual = entry.getKey();
                List<TestPlan> registrosTester = entry.getValue();

                // Adiciona o nome do tester como título
                Row testerRow = sheet.createRow(rowNum++);
                Cell testerCell = testerRow.createCell(0);
                testerCell.setCellValue("Tester: " + testerAtual);

                CellStyle boldStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
                boldFont.setBold(true);
                boldStyle.setFont(boldFont);
                testerCell.setCellStyle(boldStyle);

                // Cria o cabeçalho da tabela principal
                Row headerRow = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Filtra os dados para a tabela principal (CONCLUIDA e RETORNO)
                List<TestPlan> dadosTabelaPrincipal = registrosTester.stream()
                        .filter(item -> item.getStatus() == Status.CONCLUIDA || item.getStatus() == Status.RETORNO)
                        .collect(Collectors.toList());

                // Preenche os dados da tabela principal
                for (TestPlan item : dadosTabelaPrincipal) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getCreated_by());
                    row.createCell(1).setCellValue(item.getData().toString());
                    row.createCell(2).setCellValue(item.getJira());
                    row.createCell(3).setCellValue(item.getCallNumber());
                    row.createCell(4).setCellValue(item.getStatus().toString());
                }

                // Adiciona seções específicas para o tester
                adicionarSecaoNaoEntregues(sheet, data, testerAtual, workbook, rowNum);
                rowNum = sheet.getLastRowNum() + 1; // Atualiza o rowNum após adicionar a seção

                adicionarSecaoImpedimentos(sheet, data, testerAtual, workbook, rowNum);
                rowNum = sheet.getLastRowNum() + 1; // Atualiza o rowNum após adicionar a seção

                adicionarSecaoCriadas(sheet, data, testerAtual, workbook, rowNum);
                rowNum = sheet.getLastRowNum() + 1; // Atualiza o rowNum após adicionar a seção

                adicionarContagemStatus(sheet, data, testerAtual, workbook, rowNum);
                rowNum = sheet.getLastRowNum() + 2; // Pula uma linha entre os testers
            }

            // Ajusta o tamanho das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Converte o workbook para um array de bytes
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IOException("Erro ao gerar o arquivo Excel: " + e.getMessage(), e);
        }
    }

    private void adicionarSecaoNaoEntregues(Sheet sheet, LocalDate data, String testerName, Workbook workbook, int rowNum) {
        // Filtra os registros com status EM_PROGRESSO e created_by igual ao testerName
        List<TestPlan> naoEntregues = repository.findByCreatedByAndDataAndStatus(testerName, data, Status.EM_PROGRESSO);
        if (naoEntregues != null && !naoEntregues.isEmpty()) {
            Row tituloRow = sheet.createRow(rowNum++);
            Cell tituloCell = tituloRow.createCell(0);
            tituloCell.setCellValue("Não Entregues");

            CellStyle boldStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            tituloCell.setCellStyle(boldStyle);

            for (TestPlan item : naoEntregues) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getCreated_by());
                row.createCell(1).setCellValue(item.getData().toString());
                row.createCell(2).setCellValue(item.getJira());
                row.createCell(3).setCellValue(item.getCallNumber());
                row.createCell(4).setCellValue(item.getStatus().toString());
            }
        }
    }

    private void adicionarSecaoImpedimentos(Sheet sheet, LocalDate data, String testerName, Workbook workbook, int rowNum) {
        // Filtra os registros com status IMPEDIMENTO e created_by igual ao testerName
        List<TestPlan> impedimentos = repository.findByCreatedByAndDataAndStatus(testerName, data, Status.IMPEDIMENTO);
        if (impedimentos != null && !impedimentos.isEmpty()) {
            Row tituloRow = sheet.createRow(rowNum++);
            Cell tituloCell = tituloRow.createCell(0);
            tituloCell.setCellValue("Impedimentos");

            CellStyle boldStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            tituloCell.setCellStyle(boldStyle);

            for (TestPlan item : impedimentos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getCreated_by());
                row.createCell(1).setCellValue(item.getData().toString());
                row.createCell(2).setCellValue(item.getJira());
                row.createCell(3).setCellValue(item.getCallNumber());
                row.createCell(4).setCellValue(item.getStatus().toString());
            }
        }
    }

    private void adicionarSecaoCriadas(Sheet sheet, LocalDate data, String testerName, Workbook workbook, int rowNum) {
        // Filtra os registros criados pelo tester (usando created_by)
        List<TestPlan> criadas = repository.findByCreatedByAndData(testerName, data);
        if (criadas != null && !criadas.isEmpty()) {
            Row tituloRow = sheet.createRow(rowNum++);
            Cell tituloCell = tituloRow.createCell(0);
            tituloCell.setCellValue("Criadas");

            CellStyle boldStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);
            tituloCell.setCellStyle(boldStyle);

            for (TestPlan item : criadas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getCreated_by());
                row.createCell(1).setCellValue(item.getData().toString());
                row.createCell(2).setCellValue(item.getJira());
                row.createCell(3).setCellValue(item.getCallNumber());
                row.createCell(4).setCellValue(item.getStatus().toString());
            }
        }
    }

    private void adicionarContagemStatus(Sheet sheet, LocalDate data, String testerName, Workbook workbook, int rowNum) {
        Row tituloRow = sheet.createRow(rowNum++);
        Cell tituloCell = tituloRow.createCell(0);
        tituloCell.setCellValue("Contagem de Status");

        CellStyle boldStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        tituloCell.setCellStyle(boldStyle);

        // Contagem de status
        for (Status status : Status.values()) {
            Long count = repository.countByCreatedByAndDataAndStatus(testerName, data, status);
            Row statusRow = sheet.createRow(rowNum++);
            statusRow.createCell(0).setCellValue(status.toString()); // Exibe o nome do status
            statusRow.createCell(1).setCellValue(count != null ? count : 0); // Evita NullPointerException
        }

        // Contagem de "Criadas"
        Long criadasCount = repository.countByCreatedByAndData(testerName, data);
        Row criadasRow = sheet.createRow(rowNum++);
        criadasRow.createCell(0).setCellValue("CRIADAS");
        criadasRow.createCell(1).setCellValue(criadasCount != null ? criadasCount : 0);
    }
}