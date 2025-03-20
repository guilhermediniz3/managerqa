package com.tester.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.tester.service.RelatorioPdfService;
import com.tester.service.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private RelatorioPdfService relatorioPdfService;

    @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> gerarRelatorioExcel(
            @RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate localDate,
            @RequestParam(value = "testerName", required = false) String testerName) throws IOException {
        byte[] relatorio = relatorioService.gerarRelatorioExcel(localDate, testerName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(relatorio);
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarRelatorioPdf(
            @RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate localDate,
            @RequestParam(value = "testerName", required = false) String testerName) {
        try {
            byte[] relatorio = relatorioPdfService.gerarRelatorioPdf(localDate, testerName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.pdf");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(relatorio);
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar o PDF: " + e.getMessage()).getBytes());
        }
    }
}