package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorCreateDTO;
import pl.joajar.jlibrary.exceptions.DuplicateResourceException;
import pl.joajar.jlibrary.exceptions.NullDataProvidedException;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    List<Author> saveAll(Iterable<Author> iterator);

    Author findById(Long id) throws ResourceNotFoundException;

    Author findAtRandom() throws ResourceNotFoundException;

    Author save(AuthorCreateDTO authorCreateDTO) throws DuplicateResourceException;

    Author updateAttributesOfAuthorFound(Author author, AuthorCreateDTO authorCreateDTO);

    Author updateAttributesThenSave(Long id, AuthorCreateDTO authorCreateDTO) throws ResourceNotFoundException;

    Author updateFoundAuthor(Author author, AuthorCreateDTO authorCreateDTO) throws NullDataProvidedException;

    Author updateAuthorThenSave(Long anyLong, AuthorCreateDTO authorCreateDTO) throws ResourceNotFoundException, NullDataProvidedException;

    void delete(Long id);
}