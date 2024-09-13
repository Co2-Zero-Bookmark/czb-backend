package com.carbonhater.co2zerobookmark.common.exception;

import com.carbonhater.co2zerobookmark.common.response.CustomError;
import com.carbonhater.co2zerobookmark.common.response.CustomResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

    private ResponseEntity<CustomResponseEntity> response(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        System.out.println(new CustomResponseEntity(false, null, new CustomError(throwable.getMessage(), status)));
        return new ResponseEntity<>(
                new CustomResponseEntity(false, null, new CustomError(throwable.getMessage(), status))
                , headers, status
        );
    }
    @ExceptionHandler({
            IllegalParameterException.class,
            BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(Exception e){
        return response(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleExceptionForBadRequest(Exception e){
        return response(e, HttpStatus.NOT_FOUND);
    }

    // 모든 예외를 처리하는 메서드 추가
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
