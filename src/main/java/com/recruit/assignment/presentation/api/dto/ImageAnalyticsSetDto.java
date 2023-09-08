package com.recruit.assignment.presentation.api.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class ImageAnalyticsSetDto {

    @NotEmpty(message = "이미지 주소")
    @Size(max=200, message = "이미지 주소")
    private String imageUrl;
}
