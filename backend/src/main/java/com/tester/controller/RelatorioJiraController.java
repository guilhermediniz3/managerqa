package com.tester.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.tester.service.RelatorioJiraPdfService;
import com.tester.service.RelatorioJiraService;

@RestController
public class RelatorioJiraController {

    @Autowired
    private RelatorioJiraService relatorioJiraService;

    @Autowired
    private RelatorioJiraPdfService relatorioJiraPdfService;

    @GetMapping("/relatorio-jira/excel")
    public ResponseEntity<byte[]> gerarRelatorioJiraExcel(@RequestParam Long testSuiteId) throws IOException {
        byte[] relatorio = relatorioJiraService.gerarRelatorioJiraExcel(testSuiteId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_jira.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        headers.setCacheControl("no-cache");
        headers.setPragma("no-cache");

        return ResponseEntity.ok()
                .headers(headers)
                .body(relatorio);
    }

    @GetMapping("/relatorio-jira/pdf")
    public ResponseEntity<byte[]> gerarRelatorioJiraPdf(@RequestParam Long testSuiteId) throws IOException {
        try {
            byte[] relatorio = relatorioJiraPdfService.gerarRelatorioJiraPdf(testSuiteId);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_jira.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
            headers.setCacheControl("no-cache");
            headers.setPragma("no-cache");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(relatorio);
        } catch (DocumentException e) {
            throw new IOException("Erro ao gerar o relatório em PDF: " + e.getMessage(), e);
        }
    }
}