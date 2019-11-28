package pl.joajar.jlibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Provided data contain null or empty String.")
public class NullDataProvidedException extends RuntimeException {
    public NullDataProvidedException() {super("Provided data contain null or empty String.");}
}
