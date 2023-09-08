package com.recruit.assignment.presentation.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum ResponseCode {
    //200 OK
    SUCCESS(200, "ok", "요청 성공"),
    CREATED(201, "created", "리소스 생성 성공"),

    //400 BAD_REQUEST
    BAD_REQUEST(400, "bad_request", "요청 데이터 오류"),
    BAD_REQUEST_JSON_MAPPING(400, "bad_request_json_mapping", "요청 데이터 필드의 타입이 잘못 되었습니다."),
    BAD_REQUEST_BINDING(400, "bad_request_binding", "요청 데이터 바인딩시 에러가 발생했습니다."),
    BAD_REQUEST_TYPE_MISMATCH(400, "bad_request_type_mismatch", "요청 데이터 타입을 찾지 못했습니다."),

    //500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(500, "internal_server_error", "정의되지 않은 예외가 발생했습니다.");

    @Getter
    private final int status;

    @Getter
    private final String code;

    @Getter
    private final String description;
}
