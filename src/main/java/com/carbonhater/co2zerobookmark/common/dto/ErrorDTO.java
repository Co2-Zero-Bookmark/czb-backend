/*
package com.carbonhater.co2zerobookmark.common.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import com.carbonhater.co2zerobookmark.common.exception.CustomException;
import com.carbonhater.co2zerobookmark.common.enumType.ErrorCode;


@Data
@Builder
public class ErrorDTO {
    private String code;
    private String msg;
    private String detail;

    public static ResponseEntity<ErrorDTO> toResponseEntity(CustomException ex){
        ErrorCode errorType = ex.getErrorCode();
        String detail = ex.getDetail();

        return ResponseEntity.status(ex.getStatus())
                .body(ErrorDTO.builder()
                        .code(errorType.getCode())
                        .msg(errorType.getMsg())
                        .detail(detail)
                        .build());
    }
}
*/
