package com.tester.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import com.tester.dto.TestPlanByTechnologyDTO;
import com.tester.enuns.Status;
import com.tester.repository.TestPlanRepository;

@Service
public class ExportSprintPDFService {

    private final TestPlanRepository testPlanRepository;

    public ExportSprintPDFService(TestPlanRepository testPlanRepository) {
        this.testPlanRepository = testPlanRepository;
    }

    public byte[] exportToPdf(LocalDate dataInicio, LocalDate dataFim,
                            String tecnologia, String statusStr,
                            String developerName) throws IOException, DocumentException {
        
        Status status = parseStatus(statusStr);
        
        List<TestPlanByTechnologyDTO> data = testPlanRepository.findForExport(
            dataInicio, dataFim, tecnologia, status, developerName);

        return generatePdf(data);
    }

    private Status parseStatus(String statusStr) {
        if (statusStr == null || statusStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Converte para o enum independentemente de usar underscore ou espaço
            String normalizedStatus = statusStr.toUpperCase().replace(" ", "_");
            return Status.valueOf(normalizedStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Status inválido: '" + statusStr + "'. " +
                "Valores válidos: " + Arrays.toString(Status.values()));
        }
    }

    private byte[] generatePdf(List<TestPlanByTechnologyDTO> data) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        addTitle(document);
        addTable(document, data);
        
        document.close();
        return outputStream.toByteArray();
    }

    private void addTitle(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("Relatório de Sprints", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(15f);
        document.add(title);
        
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph date = new Paragraph("Gerado em: " + LocalDate.now(), dateFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20f);
        document.add(date);
    }

    private void addTable(Document document, List<TestPlanByTechnologyDTO> data) throws DocumentException {
        if (data.isEmpty()) {
            Font warningFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED);
            Paragraph warning = new Paragraph("Nenhum dado encontrado para os filtros aplicados", warningFont);
            warning.setAlignment(Element.ALIGN_CENTER);
            document.add(warning);
            return;
        }

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.3f, 0.8f, 0.8f, 2.5f, 1.8f, 1.8f, 1.8f, 1.8f, 1.3f});
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        addTableHeader(table);
        addTableData(table, data);
        
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
        String[] headers = {"SPRINT", "UL", "BITRIX", "DESCRIÇÃO", "DESENVOLVEDOR", "TESTER", "MÓDULO", "TECNOLOGIA", "STATUS"};
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(51, 51, 51));
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }

    private void addTableData(PdfPTable table, List<TestPlanByTechnologyDTO> data) {
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        
        for (TestPlanByTechnologyDTO item : data) {
            table.addCell(createCell(item.getSprint() != null ? item.getSprint().toString() : "", dataFont, Element.ALIGN_CENTER));
            table.addCell(createCell(item.getUl(), dataFont, Element.ALIGN_CENTER));
            table.addCell(createCell(item.getBitrix(), dataFont, Element.ALIGN_CENTER));
            table.addCell(createCell(item.getDescricao(), dataFont, Element.ALIGN_LEFT));
            table.addCell(createCell(item.getDesenvolvedor(), dataFont, Element.ALIGN_LEFT));
            table.addCell(createCell(item.getTester(), dataFont, Element.ALIGN_LEFT));
            table.addCell(createCell(item.getModulo(), dataFont, Element.ALIGN_LEFT));
            table.addCell(createTechCell(item.getTecnologia(), dataFont));
            table.addCell(createStatusCell(item.getStatus(), smallFont));
        }
    }

    private PdfPCell createCell(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content != null ? content : "", font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell createTechCell(String tecnologia, Font baseFont) {
        Font techFont = new Font(baseFont.getBaseFont(), 9);
        BaseColor color = BaseColor.BLACK;
        
        if (tecnologia != null) {
            String techUpper = tecnologia.toUpperCase();
            if (techUpper.contains("JAVA")) {
                color = BaseColor.RED;
            } else if (techUpper.contains("C#")) {
                color = new BaseColor(128, 0, 128); // Roxo
            } else if (techUpper.contains("REACT")) {
                color = new BaseColor(0, 0, 255); // Azul
            }
        }
        
        techFont.setColor(color);
        PdfPCell cell = new PdfPCell(new Phrase(tecnologia != null ? tecnologia : "", techFont));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell createStatusCell(Status status, Font baseFont) {
        Font statusFont = new Font(baseFont.getBaseFont(), 8);
        BaseColor color = BaseColor.BLACK;
        String displayText = "";
        
        if (status != null) {
            switch (status) {
                case EM_PROGRESSO:
                    color = BaseColor.BLUE;
                    displayText = "EM PROGRESSO";
                    break;
                case CONCLUIDA:
                	color = new BaseColor(0, 128, 0);
                    displayText = "CONCLUÍDO";
                    break;
                case RETORNO:
                    color = BaseColor.RED;
                    displayText = "RETORNO";
                    break;
                case IMPEDIMENTO:
                    color = new BaseColor(255, 165, 0); // Laranja
                    displayText = "IMPEDIMENTO";
                    break;
                default:
                    displayText = status.toString().replace("_", " ");
            }
        }
        
        statusFont.setColor(color);
        PdfPCell cell = new PdfPCell(new Phrase(displayText, statusFont));
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}