package bit.app.couple.exception;

import bit.app.couple.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CoupleExceptionHandler {

    @ExceptionHandler(CoupleException.CoupleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCoupleNotFoundException(CoupleException.CoupleNotFoundException e,
                                                                          HttpServletRequest request) {
        log.error("Error Code : {}, Url : {}, message : {}", HttpStatus.NOT_FOUND, request.getRequestURI(),
                e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(CoupleException.CodeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCodeNotFoundException(CoupleException.CodeNotFoundException ex,
                                                                        HttpServletRequest request) {
        log.error("Error Code : {}, Url : {}, message : {}", HttpStatus.BAD_REQUEST, request.getRequestURI(),
                ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.CoupleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCoupleAlreadyExistsException(
            CoupleException.CoupleAlreadyExistsException ex, HttpServletRequest request) {
        log.error("Error Code : {}, Url : {}, message : {}", HttpStatus.CONFLICT, request.getRequestURI(),
                ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.CouplePermissionException.class)
    public ResponseEntity<ErrorResponseDto> handleCouplePermissionException(
            CoupleException.CouplePermissionException ex, HttpServletRequest request) {
        log.error("Error Code : {}, Url : {}, message : {}", HttpStatus.FORBIDDEN, request.getRequestURI(),
                ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericCoupleException(CoupleException ex,
                                                                         HttpServletRequest request) {
        log.error("Error Code : {}, Url : {}, message : {}", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}
