package bit.couple.exception;

import bit.couple.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CoupleExceptionHandler {

    @ExceptionHandler(CoupleException.CoupleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCoupleNotFoundException(CoupleException.CoupleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.CodeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCodeNotFoundException(CoupleException.CodeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.CoupleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCoupleAlreadyExistsException(CoupleException.CoupleAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.CouplePermissionException.class)
    public ResponseEntity<ErrorResponseDto> handleCouplePermissionException(CoupleException.CouplePermissionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }

    @ExceptionHandler(CoupleException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericCoupleException(CoupleException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생하였습니다."));
    }


}