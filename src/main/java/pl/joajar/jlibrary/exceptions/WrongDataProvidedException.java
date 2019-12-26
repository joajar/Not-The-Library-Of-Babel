package pl.joajar.jlibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Provided data contain null or empty String, or is wrong.")
public class WrongDataProvidedException extends RuntimeException {
    public WrongDataProvidedException() {super("Provided data contain null or empty String, or is wrong.");}

    public WrongDataProvidedException(String message) {super(message);}
}
