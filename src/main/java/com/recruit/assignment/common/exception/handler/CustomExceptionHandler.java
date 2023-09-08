package com.recruit.assignment.common.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.recruit.assignment.common.exception.AbstractRuntimeException;
import com.recruit.assignment.common.exception.badrequest.BadRequestException;
import com.recruit.assignment.common.exception.badrequest.BindingException;
import com.recruit.assignment.common.exception.badrequest.RequestJsonMappingException;
import com.recruit.assignment.common.exception.badrequest.TypeMismatchException;
import com.recruit.assignment.common.exception.system.SystemException;
import com.recruit.assignment.presentation.api.dto.response.CustomResponse;
import com.recruit.assignment.presentation.api.dto.response.ResponseEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler {
    private final BindingResultHandler bindingResultHandler;

    /**
     * Request dto data type 변환시 에러 발생
     *
     * @param exception {@link HttpMessageNotReadableException}
     * @return {@link CustomResponse}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof JsonMappingException) {
            JsonMappingException ex = (JsonMappingException) exception.getCause();
            String errorMessage = ex.getPath()
                    .stream()
                    .map(reference -> String.format("[%s]", reference.getFieldName()))
                    .collect(Collectors.joining(", "));
            return ResponseEntityFactory.createErrorResponse(new RequestJsonMappingException(errorMessage + " 필드값을 확인해주세요."));
        }

        return ResponseEntityFactory.createErrorResponse(new BadRequestException(exception.getCause()));
    }

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생
     * RequestBody binding error 발생시 발생
     *
     * @param exception {@link MethodArgumentNotValidException}
     * @return {@link CustomResponse}
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<CustomResponse<Void>> handleBindingException(BindException exception) {
        String message = bindingResultHandler.getErrors(exception.getBindingResult())
                .stream()
                .map(formError -> String.format("[%s] %s", formError.getInputName(), formError.getMessage()))
                .collect(Collectors.joining(", "));
        return ResponseEntityFactory.createErrorResponse(new BindingException(message));
    }

    /**
     * request type 바인딩 실패시 발생
     *
     * @param exception {@link MethodArgumentTypeMismatchException}
     * @return {@link CustomResponse}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        if (exception.getCause() instanceof ConversionFailedException) {
            return ResponseEntityFactory.createErrorResponse(new TypeMismatchException(exception.getCause().getCause().getMessage(), exception.getCause()));
        }

        return ResponseEntityFactory.createErrorResponse(new TypeMismatchException(exception.getCause()));
    }

    /**
     * 주로 비즈니스 로직 예외로 발생
     *
     * @param exception {@link IllegalArgumentException}
     * @return {@link CustomResponse}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntityFactory.createErrorResponse(new BadRequestException(exception));
    }

    /**
     * Application에서 정의한 RuntimeException
     *
     * @param exception {@link AbstractRuntimeException}
     * @return {@link CustomResponse}
     */
    @ExceptionHandler(AbstractRuntimeException.class)
    public ResponseEntity<CustomResponse<Void>> handleAbstractRuntimeException(AbstractRuntimeException exception) {
        return ResponseEntityFactory.createErrorResponse(exception);
    }

    /**
     * 캐치하지 못한 나머지 에러처리
     *
     * @param exception {@link Throwable}
     * @return {@link CustomResponse}
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CustomResponse<Void>> handleThrowableException(Throwable exception) {
        return ResponseEntityFactory.createErrorResponse(new SystemException(exception));
    }
}
