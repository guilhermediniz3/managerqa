package com.tester.controller;

import com.tester.service.ExportSprintExcelService;
import com.tester.service.ExportSprintPDFService;
import com.tester.service.RelatorioPdfService;
import com.tester.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private RelatorioPdfService relatorioPdfService;
    @Autowired
    private ExportSprintExcelService exportSprintExcelService;
    
    @Autowired
    private ExportSprintPDFService exportSprintPDFService;

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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Erro ao gerar o PDF: " + e.getMessage()).getBytes());
        }
    }
    
    
    
    // dashboard excel e pdf -----------------------------------------------------------------------------------
    @GetMapping("/sprint-excel")
    public ResponseEntity<byte[]> gerarSprintExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String tecnologia,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String desenvolvedor) throws IOException {
        
        byte[] excelBytes = exportSprintExcelService.exportToExcel(
            dataInicio, dataFim, tecnologia, status, desenvolvedor);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sprints.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }

    @GetMapping("/sprint-pdf")
    public ResponseEntity<byte[]> gerarSprintPDF(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String tecnologia,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String desenvolvedor) throws IOException, DocumentException { // Adicione DocumentException aqui
        
        byte[] pdfBytes = exportSprintPDFService.exportToPdf(
            dataInicio, dataFim, tecnologia, status, desenvolvedor);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sprints.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}