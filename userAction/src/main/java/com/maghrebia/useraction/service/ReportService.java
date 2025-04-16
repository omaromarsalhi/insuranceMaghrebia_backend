package com.maghrebia.useraction.service;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.maghrebia.useraction.Repository.ReportRepository;
import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.ActionStrategy;
import com.maghrebia.useraction.entity.ReportResponse;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final TrackingService trackingService;
    private final AiService aiService;


    public ReportResponse saveReportResponse(String userId, LocalDate startDate, LocalDate endDate) {
        ReportResponse lastReport = reportRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        if (startDate == null || endDate == null) {

            if (lastReport == null) {
                throw new RuntimeException("Aucun rapport précédent trouvé pour déterminer la date de début.");
            }
            startDate = lastReport.getCreatedAt().toLocalDate();
            endDate = LocalDate.now();

        }

        List<Action> actionsInRange = trackingService.getActionsBetweenDates(userId, startDate, endDate);
        if (actionsInRange.isEmpty()) {
            throw new RuntimeException("Aucune action dans la période spécifiée pour générer un rapport.");
        }

        ReportResponse reportResponse = aiService.getAiRecommendations(actionsInRange);
        reportResponse.setUserId(userId);
        reportResponse.setCreatedAt(LocalDateTime.now());
        Map<LocalDate, Integer> filteredScores = trackingService.getUserScoresPerDay(userId, startDate, endDate);
        reportResponse.setDailyScores(filteredScores);


        if (lastReport != null) {
            Map<LocalDate, Integer> lastScores = trackingService.getUserScoresPerDay(userId, null, null);
            int evolution = calculateEngagementEvolution(lastScores, filteredScores);
            reportResponse.setEngagementEvolution(evolution);
        } else {
            reportResponse.setEngagementEvolution(0);
        }
        return reportRepository.save(reportResponse);
    }

    public List<ReportResponse> getReportsByUserId(String userId){
        return reportRepository.findByUserId(userId);
    }
    public ReportResponse getReportbyId(String reportId){
        return reportRepository.findById(reportId).get();
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

    private int calculateEngagementEvolution(Map<LocalDate, Integer> lastScores, Map<LocalDate, Integer> currentScores) {
        int lastTotal = lastScores.values().stream().mapToInt(Integer::intValue).sum();
        int currentTotal = currentScores.values().stream().mapToInt(Integer::intValue).sum();
        if (lastTotal == 0) {
            return 100;
        }

        return (int) (((double) (currentTotal - lastTotal) / lastTotal) * 100);
    }

}
