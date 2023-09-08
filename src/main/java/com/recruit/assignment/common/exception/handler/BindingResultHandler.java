package com.recruit.assignment.common.exception.handler;

import com.recruit.assignment.common.exception.util.FormError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * 바인딩 에러 관련 메세지를 구성하는 클래스
 */
@Component
@RequiredArgsConstructor
public class BindingResultHandler {

    /**
     * Gets errors.
     *
     * @param bindingResult the binding result
     * @return the errors
     */
    public List<FormError> getErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        List<FormError> formErrors = new ArrayList<>();
        for (FieldError err : errors) {
            String fieldName = err.getField();
            String rejectValue = String.valueOf(err.getRejectedValue());
            StringBuilder eMsg = new StringBuilder();
            eMsg.append(splitCamelCase(fieldName).toUpperCase()).append(" : ").append(rejectValue);
            String defaultMessage;
            if ("typeMismatch".equalsIgnoreCase(err.getCode())) {
                defaultMessage = eMsg.toString();
            } else {
                defaultMessage = err.getDefaultMessage();
            }
            FormError fe = new FormError(fieldName, defaultMessage);
            formErrors.add(fe);
        }
        return formErrors;
    }

    private String getTypeMismatchKey(String type) {
        if ("Integer".equalsIgnoreCase(type) || "Long".equalsIgnoreCase(type) || "Double".equalsIgnoreCase(type)
                || "Float".equalsIgnoreCase(type)) {
            return "admin.valid.type.mismatch.digit";
        }
        if ("Date".equalsIgnoreCase(type) || "Timestamp".equalsIgnoreCase(type)) {
            return "admin.valid.type.mismatch.date";
        }
        return "admin.valid.type.mismatch";
    }

    /**
     * Split camel case string.
     *
     * @param s the s
     * @return the string
     */
    static String splitCamelCase(String s) {
        return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }
}
