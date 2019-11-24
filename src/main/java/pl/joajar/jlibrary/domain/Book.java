package pl.joajar.jlibrary.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@Getter
@Builder
@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "^[.,\\u0119\\s\\p{Alnum}]{2,}$", message="Title should consist of at least 2 signs, taken out of the set of all alphanumeric signs, space, comma, full stop.")
    @Column(name = "title")
    private String title;

    @Size(min = 13, max = 13)
    @Column(name = "isbn")
    private String isbn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Relation> relationSet;

    public Book(String title, String isbn, LocalDate publicationDate, Relation... relationSet) {

        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;

        for (Relation relation : relationSet)
            relation.setBook(this);

        this.relationSet = Stream.of(relationSet).collect(Collectors.toSet());
    }

    public Book(Long id, String title, String isbn, LocalDate publicationDate, Relation... relationSet) {

        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;

        for (Relation relation : relationSet)
            relation.setBook(this);

        this.relationSet = Stream.of(relationSet).collect(Collectors.toSet());
    }
}
