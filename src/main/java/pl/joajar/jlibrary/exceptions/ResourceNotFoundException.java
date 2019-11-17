package pl.joajar.jlibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource has not been found.")
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {super("Resource with was not found.");}

    public ResourceNotFoundException(Long id) {
        super("Resource with id = " + id + " was not found.");
    }
}
