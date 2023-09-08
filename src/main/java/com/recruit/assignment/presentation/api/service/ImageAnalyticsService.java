package com.recruit.assignment.presentation.api.service;

import com.recruit.assignment.common.util.FileUtil;
import com.recruit.assignment.presentation.api.dto.ImageAnalyticsGetDto;
import com.recruit.assignment.presentation.api.dto.ImageAnalyticsSetDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ImageAnalyticsService {

    private static final List<String> EXTENSIONS_IMAGE = Arrays.asList("jpg", "png", "jpeg");

    /**
     * 이미지 분석 핸들러
     *
     * @param imageAnalyticsSetDto
     * @return the image analytics get dto
     */
    public ImageAnalyticsGetDto getImageAnalytics(ImageAnalyticsSetDto imageAnalyticsSetDto) {
        this.validate(imageAnalyticsSetDto);
        ImageAnalyticsGetDto imageAnalyticsGetDto = this.imageAnalytics(imageAnalyticsSetDto);
        this.imageCalculate(imageAnalyticsGetDto);
        return imageAnalyticsGetDto;
    }

    /**
     * 유효성 체크
     *
     * @param imageAnalyticsSetDto
     */
    private void validate(ImageAnalyticsSetDto imageAnalyticsSetDto) {

        // http or https 로 시작하는지 체크
        if (!StringUtils.startsWithAny(imageAnalyticsSetDto.getImageUrl(), "http://", "https://")) {
            throw new IllegalArgumentException("이미지 주소는 http:// Or https:// 로 시작해야 합니다.");
        }

        // jpg, jpeg, png 확장자인지 체크
        String fileExtension = FilenameUtils.getExtension(imageAnalyticsSetDto.getImageUrl().toLowerCase());
        if (!EXTENSIONS_IMAGE.contains(fileExtension)) {
            throw new IllegalArgumentException("유효하지않은 파일 형식입니다.");
        }

        // 이미지 존재여부, read timeout 체크
        FileUtil.checkUrl(imageAnalyticsSetDto.getImageUrl());
    }

    /**
     * 이미지 정보 조회
     *
     * @param imageAnalyticsSetDto
     * @return the image analytics get dto
     */
    public ImageAnalyticsGetDto imageAnalytics(ImageAnalyticsSetDto imageAnalyticsSetDto) {
        ImageAnalyticsGetDto imageAnalyticsGetDto;
        try {
            URL url = new URL(imageAnalyticsSetDto.getImageUrl());
            Image image = ImageIO.read(url);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            URLConnection conn = url.openConnection();
            int imageSize = conn.getContentLength();

            // byte -> kilobyte 계산
            if (!ObjectUtils.isEmpty(imageSize)) {
                imageSize = imageSize / 1024;
            }

            imageAnalyticsGetDto = ImageAnalyticsGetDto.builder()
                    .width(width)
                    .height(height)
                    .fileSize(imageSize)
                    .build();

        } catch (Exception e) {
            log.error("imageAnalytics \ntrace: {}", ExceptionUtils.getStackTrace(e));
            throw new IllegalArgumentException("이미지 정보 가져오는데 실패하였습니다.");
        }
        return imageAnalyticsGetDto;
    }

    /**
     * 이미지 계산값
     * @param imageAnalyticsGetDto
     * @return the image analytics get dto
     */
    public ImageAnalyticsGetDto imageCalculate(ImageAnalyticsGetDto imageAnalyticsGetDto) {
        int minValue;

        if (imageAnalyticsGetDto.getWidth() > imageAnalyticsGetDto.getHeight()) {
            minValue = imageAnalyticsGetDto.getHeight();
        } else {
            minValue = imageAnalyticsGetDto.getWidth();
        }

        // 전체 합계
        double total = 0.0;
        for (int i =1; i <= minValue; i++) {
            double temp = 1;
            for (int j = 1; j <= i; j++) {
                temp = temp * j;
            }
            total = total + (1 / temp);
        }

        double result = Math.round(total * 100000) / 100000.0;
        imageAnalyticsGetDto.setResultValue(result);

        return imageAnalyticsGetDto;
    }
}
