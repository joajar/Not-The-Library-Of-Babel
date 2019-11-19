package pl.joajar.jlibrary.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Setter
@Getter
@EqualsAndHashCode
@Entity
@Table(name="books_authors")
public class BookAuthorship implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonManagedReference
    private Author author;

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonManagedReference
    private Book book;

    public BookAuthorship(Author author) {
        this.author = author;
    }
}
