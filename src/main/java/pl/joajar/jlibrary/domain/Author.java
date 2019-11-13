package pl.joajar.jlibrary.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "^[.\\s\\p{Alnum}]{2,}$", message = "First name should consist of at least 2 signs, taken out of the set of all alphanumeric signs, and additionally the whitespace and the full stop.")
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "^[\\p{Alnum}]{2,}$", message = "Last name should consist of at least 2 alphanumeric signs.")
    @Column(name = "last_name")
    private String lastName;
}
