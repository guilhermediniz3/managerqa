package com.tester.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tester.entity.TestPlan;
import com.tester.enuns.Status;
import com.tester.repository.TestPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioPdfService {

    @Autowired
    private TestPlanRepository repository;

    public byte[] gerarRelatorioPdf(LocalDate data, String testerName) throws IOException, DocumentException {
        // Consulta os dados no banco
        List<TestPlan> dados;
        if (testerName != null && !testerName.isEmpty() && !testerName.equals("Todos os testers")) {
            // Busca tarefas do tester específico (incluindo criadas por ele)
            dados = repository.findByTesterNameAndData(testerName, data);
            List<TestPlan> criadas = repository.findCriadasByCreatedByAndData(testerName, data);
            dados.addAll(criadas); // Adiciona as tarefas criadas pelo tester
        } else {
            // Busca todas as tarefas da data
            dados = repository.findByData(data);
        }

        // Verifica se há dados
        if (dados == null || dados.isEmpty()) {
            throw new IOException("Nenhum dado encontrado para a data especificada.");
        }

        // Cria o documento PDF
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Adiciona um título ao documento
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Relatório de Testes", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Adiciona a data ao documento
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Paragraph dateParagraph = new Paragraph("Data: " + data.toString(), dateFont);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(dateParagraph);

        // Adiciona espaço entre o título e a tabela
        document.add(new Paragraph(" "));

        // Agrupa os dados por tester (tester.name ou created_by)
        Map<String, List<TestPlan>> dadosAgrupadosPorTester = dados.stream()
                .filter(item -> item.getTester() != null || item.getCreated_by() != null) // Filtra registros com tester ou created_by nulo
                .collect(Collectors.groupingBy(item -> 
                    item.getTester() != null ? item.getTester().getName() : item.getCreated_by()));

        // Itera sobre cada tester e seus registros
        for (Map.Entry<String, List<TestPlan>> entry : dadosAgrupadosPorTester.entrySet()) {
            String testerAtual = entry.getKey();
            List<TestPlan> registrosTester = entry.getValue();

            // Adiciona o nome do tester como título
            adicionarTitulo(document, "Tester: " + testerAtual);

            // Cria a tabela principal
            PdfPTable tabelaPrincipal = new PdfPTable(5); // 5 colunas
            tabelaPrincipal.setWidthPercentage(100);

            // Define os cabeçalhos da tabela
            String[] headers = {"Tester", "Data", "Jira", "Call Number", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER); // Sem borda
                tabelaPrincipal.addCell(cell);
            }

            // Filtra os dados para a tabela principal (CONCLUIDA e RETORNO)
            List<TestPlan> dadosTabelaPrincipal = registrosTester.stream()
                    .filter(item -> item.getStatus() == Status.CONCLUIDA || item.getStatus() == Status.RETORNO)
                    .collect(Collectors.toList());

            // Preenche os dados da tabela principal
            for (TestPlan item : dadosTabelaPrincipal) {
                tabelaPrincipal.addCell(createCell(testerAtual)); // Usa o nome do tester ou created_by
                tabelaPrincipal.addCell(createCell(item.getData().toString()));
                tabelaPrincipal.addCell(createCell(item.getJira()));
                tabelaPrincipal.addCell(createCell(item.getCallNumber()));
                tabelaPrincipal.addCell(createCell(item.getStatus().toString()));
            }

            document.add(tabelaPrincipal);
            document.add(new Paragraph(" ")); // Espaçamento após a tabela

            // Adiciona seções específicas para o tester
            adicionarSecaoNaoEntregues(document, data, testerAtual);
            adicionarSecaoImpedimentos(document, data, testerAtual);
            adicionarSecaoCriadas(document, data, testerAtual);
            adicionarContagemStatus(document, data, testerAtual);

            // Adiciona um espaço entre os testers
            document.add(new Paragraph(" "));
        }

        document.close();
        return outputStream.toByteArray();
    }

    private void adicionarTitulo(Document document, String tituloTexto) throws DocumentException {
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(tituloTexto, fontTitulo));
        cell.setBackgroundColor(BaseColor.GRAY); // Fundo cinza
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centralizado
        cell.setBorder(Rectangle.NO_BORDER); // Sem borda
        cell.setPadding(10); // Espaçamento interno
        PdfPTable tabelaTitulo = new PdfPTable(1);
        tabelaTitulo.setWidthPercentage(100);
        tabelaTitulo.addCell(cell);
        document.add(tabelaTitulo);
        document.add(new Paragraph(" ")); // Espaçamento após o título
    }

    private PdfPCell createCell(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centralizado
        cell.setBorder(Rectangle.NO_BORDER); // Sem borda
        return cell;
    }

    private void adicionarSecaoNaoEntregues(Document document, LocalDate data, String testerName) throws DocumentException {
        List<TestPlan> naoEntregues = repository.findByTesterNameAndDataAndStatus(testerName, data, Status.EM_PROGRESSO);
        if (naoEntregues != null && !naoEntregues.isEmpty()) {
            adicionarTitulo(document, "Não Entregues");

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);

            for (TestPlan item : naoEntregues) {
                // Verifica se a tarefa foi criada pelo tester (created_by preenchido)
                if (item.getCreated_by() != null && !item.getCreated_by().isEmpty()) {
                    tabela.addCell(createCell(testerName)); // Adiciona uma coluna "Criada"
                } else {
                    tabela.addCell(createCell(testerName)); // Usa o nome do tester
                }
                tabela.addCell(createCell(item.getData().toString()));
                tabela.addCell(createCell(item.getJira()));
                tabela.addCell(createCell(item.getCallNumber()));
                tabela.addCell(createCell(item.getStatus().toString()));
            }

            document.add(tabela);
            document.add(new Paragraph(" ")); // Espaçamento após a tabela
        }
    }

    private void adicionarSecaoImpedimentos(Document document, LocalDate data, String testerName) throws DocumentException {
        List<TestPlan> impedimentos = repository.findByTesterNameAndDataAndStatus(testerName, data, Status.IMPEDIMENTO);
        if (impedimentos != null && !impedimentos.isEmpty()) {
            adicionarTitulo(document, "Impedimentos");

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);

            for (TestPlan item : impedimentos) {
                tabela.addCell(createCell(testerName)); // Usa o nome do tester
                tabela.addCell(createCell(item.getData().toString()));
                tabela.addCell(createCell(item.getJira()));
                tabela.addCell(createCell(item.getCallNumber()));
                tabela.addCell(createCell(item.getStatus().toString()));
            }

            document.add(tabela);
            document.add(new Paragraph(" ")); // Espaçamento após a tabela
        }
    }

    private void adicionarSecaoCriadas(Document document, LocalDate data, String testerName) throws DocumentException {
        List<TestPlan> criadas = repository.findCriadasByCreatedByAndData(testerName, data);
        if (criadas != null && !criadas.isEmpty()) {
            adicionarTitulo(document, "Criadas");

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);

            for (TestPlan item : criadas) {
                tabela.addCell(createCell(testerName)); // Usa o nome do tester
                tabela.addCell(createCell(item.getData().toString()));
                tabela.addCell(createCell(item.getJira()));
                tabela.addCell(createCell(item.getCallNumber()));
                tabela.addCell(createCell(item.getStatus().toString()));
            }

            document.add(tabela);
            document.add(new Paragraph(" ")); // Espaçamento após a tabela
        }
    }

    private void adicionarContagemStatus(Document document, LocalDate data, String testerName) throws DocumentException {
        adicionarTitulo(document, "Contagem de Status");

        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(50); // Tabela mais estreita
        tabela.setHorizontalAlignment(Element.ALIGN_CENTER); // Centralizada na página

        // Contagem de status
        for (Status status : Status.values()) {
            Long count = repository.countByTesterNameAndDataAndStatus(testerName, data, status);
            tabela.addCell(createCell(status.toString()));
            tabela.addCell(createCell(String.valueOf(count != null ? count : 0)));
        }

        // Contagem de "Criadas" (usando created_by)
        Long criadasCount = repository.countCriadasByCreatedByAndData(testerName, data);
        tabela.addCell(createCell("CRIADAS"));
        tabela.addCell(createCell(String.valueOf(criadasCount != null ? criadasCount : 0)));

        document.add(tabela);
    }
}