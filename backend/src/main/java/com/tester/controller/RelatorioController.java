package com.tester.controller;

import com.tester.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    /**
     * Endpoint para gerar um relatório Excel com base na data e no nome do tester.
     *
     * @param localDate  Data no formato dd/MM/yyyy
     * @param testerName Nome do tester (opcional)
     * @return Arquivo Excel como resposta HTTP
     * @throws IOException Se ocorrer um erro ao gerar o relatório
     */
    @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> gerarRelatorioExcel(
            @RequestParam("data") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate localDate,
            @RequestParam(value = "testerName", required = false) String testerName) throws IOException {

        // Gera o relatório
        byte[] relatorio = relatorioService.gerarRelatorioExcel(localDate, testerName);

        // Configura os cabeçalhos para download do arquivo
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) // Tipo MIME correto
                .body(relatorio);
    }
}