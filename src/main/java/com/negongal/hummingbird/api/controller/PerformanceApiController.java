package com.negongal.hummingbird.api.controller;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.service.S3Service;
import com.negongal.hummingbird.service.PerformanceService;
import com.negongal.hummingbird.service.TicketingService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceApiController {

    private final PerformanceService performanceService;
    private final TicketingService ticketService;
    private final S3Service fileService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> register(
                @Valid @RequestPart(value = "performance") PerformanceRequestDto requestDto,
                @RequestPart(required = false, value = "photo") MultipartFile photo) throws IOException {
        String photoUrl = (photo == null) ? null : fileService.saveFile(photo);
        Long performanceId = performanceService.save(requestDto, photoUrl);
        ticketService.save(performanceId, requestDto);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<HashMap<String, List<PerformanceDto>>> getPerformances() {
        List<PerformanceDto> performanceList = performanceService.findAll();
        HashMap<String, List<PerformanceDto>> responseList = new HashMap<>();
        responseList.put("performance_list", performanceList);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/detail/{performanceId}")
    public ResponseEntity<PerformanceDto> getPerformanceDetail(@PathVariable Long performanceId) {
        PerformanceDto Performance = performanceService.findOne(performanceId);
        return new ResponseEntity<>(Performance, HttpStatus.OK);
    }
}