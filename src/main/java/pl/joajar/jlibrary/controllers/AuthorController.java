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
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/library/authors")
public class AuthorController {
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> getAllAuthors() {
        LOG.info("AuthorController.getAllAuthors(): finding all authors from the library.");
        return new ResponseEntity<>(authorService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        LOG.info("AuthorController.getAuthorById: finding the author with id = {}.", id);
        return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorAtRandom() {
        LOG.info("AuthorController.getAuthorAtRandom: finding one author at random.");
        return new ResponseEntity<>(authorService.findAtRandom(), HttpStatus.OK);
    }
}
