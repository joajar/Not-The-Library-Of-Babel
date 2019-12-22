package pl.joajar.jlibrary.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
class ErrorResponse {
    private HttpStatus status;
    private String message;
}
