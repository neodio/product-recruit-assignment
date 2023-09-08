package com.recruit.assignment.presentation.api.service;

import com.recruit.assignment.presentation.api.dto.ImageAnalyticsGetDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ImageAnalyticsServiceTest {

    @Autowired
    private ImageAnalyticsService imageAnalyticsService;

    @Test
    @DisplayName("이미지 계산 5px")
    public void imageCalculateTest5px() {
        // given
        ImageAnalyticsGetDto imageAnalyticsGetDto = new ImageAnalyticsGetDto();
        imageAnalyticsGetDto.setWidth(5);
        imageAnalyticsGetDto.setHeight(100000);

        // when
        imageAnalyticsService.imageCalculate(imageAnalyticsGetDto);

        // then
        assertEquals(1.71667, imageAnalyticsGetDto.getResultValue());
    }

    @Test
    @DisplayName("이미지 계산 10px")
    public void imageCalculateTest10px() {
        // given
        ImageAnalyticsGetDto imageAnalyticsGetDto = new ImageAnalyticsGetDto();
        imageAnalyticsGetDto.setWidth(10);
        imageAnalyticsGetDto.setHeight(100000);

        // when
        imageAnalyticsService.imageCalculate(imageAnalyticsGetDto);

        // then
        assertEquals(1.71828, imageAnalyticsGetDto.getResultValue());
    }
}
