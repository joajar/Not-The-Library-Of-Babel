package pl.joajar.jlibrary.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.services.BookService;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/library/books")
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(BookController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getAllBooks() {
        LOG.info("BookController.getAllBooks: finding all authors from the library.");
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/publicationyear/{publicationYear}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getBookByPublicationYear(@PathVariable("publicationYear") String publicationYear) {
        LOG.info("BookController.getBookByPublicationYear: finding books published in year {}.", publicationYear);
        return new ResponseEntity<>(bookService.findByPublicationYear(publicationYear), HttpStatus.OK);
    }
}
