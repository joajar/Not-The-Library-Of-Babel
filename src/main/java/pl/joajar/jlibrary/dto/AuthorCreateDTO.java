package pl.joajar.jlibrary.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthorCreateDTO {
    @NonNull
    private String firstName, lastName;
}
