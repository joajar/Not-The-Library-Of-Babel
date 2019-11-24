package pl.joajar.jlibrary.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookWithAuthorSetDTO {
    private Long id;

    @Pattern(regexp = "^[.,\\u0119\\s\\p{Alnum}]{2,}$", message="Title should consist of at least 2 signs, taken out of the set of all alphanumeric signs, space, comma, full stop.")
    private String title;

    @Size(min = 13, max = 13)
    private String isbn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private Set<AuthorDTO> authorSet = new HashSet<>();
}
