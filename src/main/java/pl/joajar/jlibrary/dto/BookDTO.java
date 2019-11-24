package pl.joajar.jlibrary.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDTO extends BookCreateDTO {
    @NonNull
    private Long id;
}
