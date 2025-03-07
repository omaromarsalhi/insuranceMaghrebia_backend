package com.maghrebia.useraction.service;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.maghrebia.useraction.Repository.ReportRepository;
import com.maghrebia.useraction.entity.ActionStrategy;
import com.maghrebia.useraction.entity.ReportResponse;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final AiService aiService;

//    public ReportResponse getAiRecommendations(String userId) {
//        String url = FLASK_API_URL + userId;
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
//        try {
//            return objectMapper.readValue(response.getBody(), ReportResponse.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Erreur lors de la conversion de la réponse de l'IA : " + e.getMessage(), e);
//        }
//    }
    public ReportResponse saveReportResponse(String userId){
        ReportResponse reportResponse =  aiService.getAiRecommendations(userId);
        reportResponse.setUserId(userId);
        reportResponse.setCreatedAt(LocalDateTime.now());
        return reportRepository.save(reportResponse);
    }

    public List<ReportResponse> getReportsByUserId(String userId){
        return reportRepository.findByUserId(userId);
    }

    public byte[] generatePdfReport(ReportResponse reportResponse) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);


        document.add(new Paragraph("Rapport Comportemental de l'Utilisateur"));
        document.add(new Paragraph("Analyse: " + reportResponse.getUserAnalysis()));
        document.add(new Paragraph("Classification: " + reportResponse.getClassification()));
        document.add(new Paragraph(" "));

        Map<String, List<ActionStrategy>> strategiesMap = reportResponse.getActions();
        if (strategiesMap != null) {
            for (Map.Entry<String, List<ActionStrategy>> entry : strategiesMap.entrySet()) {
                String type = entry.getKey();
                document.add(new Paragraph("Stratégies pour " + type + ":"));
                for (ActionStrategy strategy : entry.getValue()) {
                    document.add(new Paragraph("- " + strategy.getDescription()));
                }
                document.add(new Paragraph(" "));
            }
        }
        document.close();
        return baos.toByteArray();
    }
}
