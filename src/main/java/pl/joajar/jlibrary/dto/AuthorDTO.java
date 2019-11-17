package pl.joajar.jlibrary.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDTO {
    @NonNull
    private String firstName, lastName;
}
