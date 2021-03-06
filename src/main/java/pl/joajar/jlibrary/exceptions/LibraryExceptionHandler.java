package pl.joajar.jlibrary.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LibraryExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryExceptionHandler.class);

    @ExceptionHandler(value = ResourceNotFoundException.class) // 404
    protected ResponseEntity<Object> handleResourceNotFound() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource has not been found."
        );
        LOG.info("LibraryExceptionHandler.handleResourceNotFound: throwing ResourceNotFoundException, resource does not exist.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateResourceException.class) // 409
    protected ResponseEntity<Object> handleDuplicateResource() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                "Resource already exist at the database."
        );
        LOG.info("LibraryExceptionHandler.handleDuplicateResource: throwing DuplicateResourceException, resource already exist.");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = WrongDataProvidedException.class) // 406
    protected ResponseEntity<Object> handleWrongDataProvided() {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Provided data contain null or empty String, or is wrong."
        );
        LOG.info("LibraryExceptionHandler.handleWrongDataProvided: throwing NullDataProvidedException, provided data contain null or empty String, or is wrong.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
