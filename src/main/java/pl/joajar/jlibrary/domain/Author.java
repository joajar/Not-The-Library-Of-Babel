package pl.joajar.jlibrary.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor
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
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Pattern(regexp = "^[.\\u0119\\s\\p{Alnum}]{2,}$", message = "First name should consist of at least 2 signs, taken out of the set of all alphanumeric signs, and additionally the whitespace and the full stop.")
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "^[\\u0119\\p{Alnum}]{2,}$", message = "Last name should consist of at least 2 alphanumeric signs.")
    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonBackReference
//    see: https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
//    https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
    private Set<Relation> relationSet;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
