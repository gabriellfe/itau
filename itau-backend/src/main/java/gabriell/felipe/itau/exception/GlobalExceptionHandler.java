package gabriell.felipe.itau.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gabriell.felipe.itau.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String errors = ex.getBindingResult().getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()).get(0);
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessageError(errors);
		return ResponseEntity.badRequest().body(errorDto);
	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<ErrorDto> handleJWTVerificationException(JwtException ex) {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setError(ex.getError());
		errorDto.setMessageError(ex.getMessage());
		return ResponseEntity.badRequest().body(errorDto);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleException(Exception ex) {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setError(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errorDto.setMessageError(ex.getMessage());
		return ResponseEntity.internalServerError().body(errorDto);
	}
}
