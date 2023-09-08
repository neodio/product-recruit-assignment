package com.recruit.assignment.common.exception.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * BindingException 발생 시 결과를 담을 클래스
 */
@Setter
@Getter
@RequiredArgsConstructor
public class FormError {
    private static final long serialVersionUID = 3832080867806332599L;

    private final String inputName;
    private final String message;
}
