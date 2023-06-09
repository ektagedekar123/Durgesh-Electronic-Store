package com.lcwd.electronicstore.exception;

import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.validate.ImageNameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger= LoggerFactory.getLogger(ImageNameValidator.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException ex){

 //       String message = ex.getMessage();
 //       ApiResponse apiResponse=new ApiResponse(message, false, HttpStatus.NOT_FOUND);

        logger.info("Exception handler is invoked!!");
        ApiResponse apiResponse = ApiResponse.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map<String, Object> response=new HashMap<>();

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        allErrors.stream().forEach(objectError -> {

            String fieldName = ((FieldError)objectError).getField();
            String message = objectError.getDefaultMessage();

            response.put(fieldName, message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponse> handlingBadApiRequest(BadApiRequestException ex){

        logger.info("Bad Api request");
        ApiResponse apiResponse=ApiResponse.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
