package com.tester.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tester.entity.TestCaseEntity;
import com.tester.entity.TestSuite;
import com.tester.repository.TestSuiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RelatorioJiraPdfService {

    @Autowired
    private TestSuiteRepository testSuiteRepository;

    public byte[] gerarRelatorioJiraPdf(Long testSuiteId) throws IOException, DocumentException {
        TestSuite testSuite = testSuiteRepository.findById(testSuiteId)
                .orElseThrow(() -> new IOException("Test Suite não encontrada com o ID: " + testSuiteId));

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Configuração da fonte
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        // Adicionar título
        Paragraph title = new Paragraph("Relatório Jira", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Adicionar espaço
        document.add(new Paragraph(" "));

        // Adicionar dados da Test Suite em uma tabela
        PdfPTable table = new PdfPTable(2); // 2 colunas
        table.setWidthPercentage(100); // Largura da tabela
        table.setSpacingBefore(10f); // Espaço antes da tabela
        table.setSpacingAfter(10f); // Espaço depois da tabela

        // Configuração das células
        table.getDefaultCell().setBorder(PdfPCell.BOX); // Adiciona bordas às células
        table.getDefaultCell().setPadding(5); // Espaçamento interno das células

        // Adicionar dados
        addRow(table, "Data", testSuite.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), headerFont, dataFont);

        // Responsável (nome do tester)
        String responsavel = testSuite.getTestPlan().getTester().getName();
        if (responsavel == null || responsavel.isEmpty()) {
            responsavel = "N/A"; // Valor padrão caso o nome do tester seja nulo ou vazio
        }
        addRow(table, "Responsável", responsavel, headerFont, dataFont);

        addRow(table, "UL", testSuite.getTestPlan().getJira(), headerFont, dataFont);
        addRow(table, "Bitrix", testSuite.getTestPlan().getCallNumber(), headerFont, dataFont);
        addRow(table, "Matriz", testSuite.getTestPlan().getMatriz(), headerFont, dataFont);
        addRow(table, "Usuário", testSuite.getTestPlan().getUserName(), headerFont, dataFont);
        addRow(table, "Senha", testSuite.getTestPlan().getPassword(), headerFont, dataFont);
        addRow(table, "Descrição", testSuite.getTestPlan().getName(), headerFont, dataFont);

        document.add(table);

        // Adicionar espaço
        document.add(new Paragraph(" "));

        // Adicionar casos de teste em uma tabela
        PdfPTable testCaseTable = new PdfPTable(5); // 5 colunas
        testCaseTable.setWidthPercentage(100); // Largura da tabela
        testCaseTable.setSpacingBefore(10f); // Espaço antes da tabela
        testCaseTable.setSpacingAfter(10f); // Espaço depois da tabela

        // Configuração das células
        testCaseTable.getDefaultCell().setBorder(PdfPCell.BOX); // Adiciona bordas às células
        testCaseTable.getDefaultCell().setPadding(5); // Espaçamento interno das células

        // Adicionar cabeçalho dos casos de teste
        addHeaderCell(testCaseTable, "Cenário", headerFont);
        addHeaderCell(testCaseTable, "Resultado Esperado", headerFont);
        addHeaderCell(testCaseTable, "Resultado Obtido", headerFont);
        addHeaderCell(testCaseTable, "Evidência (Vimeo)", headerFont);
        addHeaderCell(testCaseTable, "Resultado", headerFont);

        // Adicionar dados dos casos de teste
        Set<TestCaseEntity> testCaseSet = testSuite.getCases();
        List<TestCaseEntity> testCases = new ArrayList<>(testCaseSet);
        for (TestCaseEntity testCase : testCases) {
            addCell(testCaseTable, testCase.getScenario(), dataFont);
            addCell(testCaseTable, testCase.getExpectedResult(), dataFont);
            addCell(testCaseTable, testCase.getObtainedResult(), dataFont);
            addCell(testCaseTable, testCase.getVideoEvidence(), dataFont);
            addCell(testCaseTable, testCase.getStatus().toString(), dataFont);
        }

        document.add(testCaseTable);

        document.close();
        return outputStream.toByteArray();
    }

    // Métodos auxiliares para adicionar células
    private void addRow(PdfPTable table, String header, String value, Font headerFont, Font dataFont) {
        PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
        headerCell.setBorder(PdfPCell.BOX);
        table.addCell(headerCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, dataFont));
        valueCell.setBorder(PdfPCell.BOX);
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setBorder(PdfPCell.BOX);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String value, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setBorder(PdfPCell.BOX);
        table.addCell(cell);
    }
}