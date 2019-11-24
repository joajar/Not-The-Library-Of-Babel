package pl.joajar.jlibrary.dto;

import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class AuthorDTO extends AuthorCreateDTO {
    @NonNull
    private Long id;

    public AuthorDTO(String firstName, String lastName, Long id) {
        super(firstName, lastName);
        this.id = id;
    }
}
