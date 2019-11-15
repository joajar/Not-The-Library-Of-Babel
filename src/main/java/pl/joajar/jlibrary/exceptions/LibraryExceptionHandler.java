package pl.joajar.jlibrary.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class LibraryExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryExceptionHandler.class);

    @ExceptionHandler(value = ResourceNotFoundException.class) // 404
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND, ex.getMessage(), Collections.singletonList("Resource does not exist.")
        );
        LOG.info("LibraryExceptionHandler.handleResourceNotFound: throwing ResourceNotFoundException, resource does not exist.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
