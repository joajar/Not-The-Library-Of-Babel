package pl.joajar.jlibrary.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.domain.Relation;
import pl.joajar.jlibrary.services.AuthorService;
import pl.joajar.jlibrary.services.BookService;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class AnnotationDrivenContextStartedListener {
    private AuthorService authorService;
    private BookService bookService;

    @Autowired
    public AnnotationDrivenContextStartedListener(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillInDatabase(){
        String secondLetterOfPolishAlphabet = "\u0105";
        String eighthLetterOfPolishAlphabet = "\u0119";
        String twentyNinthLetterOfPolishAlphabet = "\u00f3";
        final String modernJavaBookTitle = "Nowoczesne receptury w Javie. Proste rozwi" + secondLetterOfPolishAlphabet + "zania trudnych problem" + twentyNinthLetterOfPolishAlphabet + "w";
        final String restBookTitle = "REST. Najlepsze praktyki i wzorce w j" + eighthLetterOfPolishAlphabet + "zyku Java";

        Author Horstmann = Author.builder().id(1L).firstName("Cay S.").lastName("Horstmann").build();
        Author Bloch = Author.builder().id(2L).firstName("Joshua").lastName("Bloch").build();
        Author Bauer = Author.builder().id(3L).firstName("Christian").lastName("Bauer").build();
        Author King = Author.builder().id(4L).firstName("Gavin").lastName("King").build();
        Author Gregory = Author.builder().id(5L).firstName("Gary").lastName("Gregory").build();
        Author Walls = Author.builder().id(6L).firstName("Craig").lastName("Walls").build();
        Author Mehta = Author.builder().id(7L).firstName("Bhakti").lastName("Mehta").build();
        Author Kousen = Author.builder().id(8L).firstName("Ken").lastName("Kousen").build();

        authorService.saveAll(Arrays.asList(Horstmann, Bloch, Bauer, King, Gregory, Walls, Mehta, Kousen));

        bookService.save(
                new Book("Java. Podstawy. Wydanie X", "9788328324800", LocalDate.of(2016, 9, 26),
                        new Relation(Horstmann)));

        bookService.save(
                new Book("Java. Techniki zaawansowane. Wydanie X", "9788328334809", LocalDate.of(2017, 9, 28),
                        new Relation(Horstmann)));

        bookService.save(
                new Book("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II",  "9788328327832", LocalDate.of(2016, 12, 13),
                        new Relation(Bauer), new Relation(King), new Relation(Gregory)));

        bookService.save(
                new Book("Java. Efektywne programowanie. Wydanie III", "9788328345775", LocalDate.of(2018, 8, 17),
                        new Relation(Bloch)));

        bookService.save(
                new Book("Spring in Action, Fifth Edition", "9781617294945", LocalDate.of(2019, 10, 1),
                        new Relation(Walls)));

        bookService.save(
                new Book(restBookTitle, "9788328306448", LocalDate.of(2015, 6, 16),
                        new Relation(Mehta)));

        bookService.save(
                new Book(modernJavaBookTitle, "9788328340732", LocalDate.of(2018, 4, 13),
                        new Relation(Kousen)));
    }
}
