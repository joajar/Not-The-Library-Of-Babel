package pl.joajar.jlibrary.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@ToString
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
    private Set<BookAuthorship> bookAuthorshipSet;

    public Book(String title, String isbn, LocalDate publicationDate, BookAuthorship... bookAuthorshipSet) {

        this.title = title;
        this.isbn = isbn;
        this.publicationDate = publicationDate;

        for (BookAuthorship bookAuthorship : bookAuthorshipSet)
            bookAuthorship.setBook(this);

        this.bookAuthorshipSet = Stream.of(bookAuthorshipSet).collect(Collectors.toSet());
    }
}
