package pl.joajar.jlibrary.controllers;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorDTO;
import pl.joajar.jlibrary.services.AuthorService;

import javax.validation.Valid;
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
        LOG.info("AuthorController.getAllAuthors: finding all authors from the library.");
        return new ResponseEntity<>(authorService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id) {
        LOG.info("AuthorController.getAuthorById: finding the author with id = {}.", id);
        return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorAtRandom() {
        LOG.info("AuthorController.getAuthorAtRandom: finding one author at random.");
        return new ResponseEntity<>(authorService.findAtRandom(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> postAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        LOG.info("AuthorController.postAuthor: posting the author with the first name {} and the last name {}.",
                authorDTO.getFirstName(), authorDTO.getLastName());
        return new ResponseEntity<>(authorService.save(authorDTO), HttpStatus.CREATED);
    }

    //For usage when *some* new properties (firstName or lastName) are null, and only non-null properties are replacing the old ones
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> patchAuthor(@PathVariable("id") Long id, @RequestBody @NonNull AuthorDTO authorDTO) {
        LOG.info("AuthorController.patchAuthor: updating all attributes of the author with id = {} (possibly some attributes only).", id);
        return new ResponseEntity<>(authorService.updateAttributesThenSave(id, authorDTO), HttpStatus.OK);
    }

    //For usage in case *all* new properties (firstName, lastName) are non-null, and have to replace the old ones
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> putAuthor(@PathVariable("id") Long id, @RequestBody @NonNull AuthorDTO authorDTO) {
        LOG.info("AuthorController.putAuthor: updating the author with id = {}.", id);
        return new ResponseEntity<>(authorService.updateAuthorThenSave(id, authorDTO), HttpStatus.OK);
    }

    //Below you may find the idea of HttpStatus.NO_CONTENT usage and customerService.deleteAuthor taken from Walls' book "Spring in Action, 5ed", p.148
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long id) {
        LOG.info("AuthorController.deleteAuthor: deleting the author with id = {} or checking there is no author under this id.", id);
        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
