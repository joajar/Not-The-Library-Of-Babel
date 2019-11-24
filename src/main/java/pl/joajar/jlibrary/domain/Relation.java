package pl.joajar.jlibrary.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@Entity
@Table(name="books_authors")
public class Relation implements Serializable {
    /* Adding ID seems to be a nonstandard solution, however IMHO necessary for convenient management of bidirectional relation:
     * Book -> Author(s) and Author -> Book(s)
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id") // = kolumna złączenia, klucz obcy; adnotacja przesłania klucz obcy
    @JsonManagedReference
    private Author author;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonManagedReference
    private Book book;

    public Relation(Author author) {
        this.author = author;
    }
}
