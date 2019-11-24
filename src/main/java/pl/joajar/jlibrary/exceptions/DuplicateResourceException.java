package pl.joajar.jlibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource already exists at the database.")
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String... strings) {

        super("Resource (" +
                String.join(", ", strings) +
                ") already exist at the database.");
    }

}
