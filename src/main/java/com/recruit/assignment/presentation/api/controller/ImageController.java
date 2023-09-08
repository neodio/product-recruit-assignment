package com.recruit.assignment.presentation.api.controller;

import com.recruit.assignment.presentation.api.dto.ImageAnalyticsGetDto;
import com.recruit.assignment.presentation.api.dto.ImageAnalyticsSetDto;
import com.recruit.assignment.presentation.api.dto.response.CustomResponse;
import com.recruit.assignment.presentation.api.dto.response.ResponseCode;
import com.recruit.assignment.presentation.api.dto.response.ResponseEntityFactory;
import com.recruit.assignment.presentation.api.service.ImageAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageAnalyticsService imageAnalyticsService;

    @PostMapping("/getImageAnalytics")
    public ResponseEntity<CustomResponse<ImageAnalyticsGetDto>> getImageAnalytics(@Valid @RequestBody ImageAnalyticsSetDto imageAnalyticsSetDto) {
        return ResponseEntityFactory.createResponse(
                ResponseCode.SUCCESS,
                imageAnalyticsService.getImageAnalytics(imageAnalyticsSetDto)
        );
    }
}
