package com.negongal.hummingbird.api.controller;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.service.PerformanceService;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PerformanceApiController {

    private final PerformanceService performanceService;

    @PostMapping(value = "/performance", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> register(
                @Valid @RequestPart(value = "performance") PerformanceRequestDto requestDto,
                @RequestPart(required = false, value = "photo") MultipartFile file) {
        /**
         * 파일 등록 - S3 이용 추가 필요
         */

        performanceService.save(requestDto);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    @GetMapping("/performance")
    public ResponseEntity<HashMap<String, List<PerformanceDto>>> getPerformances() {
        List<PerformanceDto> performanceList = performanceService.findAll();
        HashMap<String, List<PerformanceDto>> responseList = new HashMap<>();
        responseList.put("performance_list", performanceList);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
