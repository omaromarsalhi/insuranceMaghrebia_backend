package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ResponseComplaint;
import com.maghrebia.complaint.service.ComplaintService;
import com.maghrebia.complaint.service.ResponseService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/complaintResponse")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ResponseController {

    private  final ResponseService responseService;

    @PostMapping("/{responderId}/{complaintId}")
    public ResponseEntity<ResponseComplaint> addResponse(@RequestBody ResponseComplaint responseComplaint,
                                                 @PathVariable String responderId,
                                                 @PathVariable String complaintId
    ){
        return ResponseEntity.ok(responseService.addRespons(responseComplaint,responderId,complaintId));
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<ResponseComplaint> getResponsesByComplainId(
            @PathVariable String complaintId
    ){
        return ResponseEntity.ok(responseService.getResponsesByComplainId(complaintId));
    }


    @DeleteMapping("")
    public Response deleteResponse(@RequestBody ResponseComplaint responseComplaint
    ){
        responseService.deleteResponse(responseComplaint);
        return Response.ok().build();
    }

}
