package pl.joajar.jlibrary.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
class BookCreateDTO {
    @Pattern(regexp = "^[.,\\u0119\\s\\p{Alnum}]{2,}$", message="Title should consist of at least 2 signs, taken out of the set of all alphanumeric signs, space, comma, full stop.")
    private String title;

    @Size(min = 13, max = 13)
    private String isbn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;
}
