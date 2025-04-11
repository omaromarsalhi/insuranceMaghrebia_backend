package com.maghrebia.useraction.controller;

import com.maghrebia.useraction.entity.Action;
import com.maghrebia.useraction.entity.ReportResponse;
import com.maghrebia.useraction.service.ReportService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportResponse reportResponse) {
        try {
            byte[] pdfBytes = reportService.generatePdfReport(reportResponse);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "user_report.pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> saveReportResponse(@PathVariable String userId) {
        return ResponseEntity.ok(reportService.saveReportResponse(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRepportsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(reportService.getReportsByUserId(userId));
    }


    @GetMapping("/getReportResponse/{reportId}")
    public ResponseEntity<?> getRepportResponse(@PathVariable String reportId) {
        System.out.println(reportId);
        return ResponseEntity.ok(reportService.getReportbyId(reportId));
    }
}
