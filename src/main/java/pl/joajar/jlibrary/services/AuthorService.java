package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    List<Author> saveAll(Iterable<Author> iterator);

    Author findById(Long id) throws ResourceNotFoundException;

    Author findAtRandom() throws ResourceNotFoundException;
}
