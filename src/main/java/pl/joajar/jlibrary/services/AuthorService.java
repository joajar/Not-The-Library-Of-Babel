package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorCreateDTO;
import pl.joajar.jlibrary.exceptions.DuplicateResourceException;
import pl.joajar.jlibrary.exceptions.WrongDataProvidedException;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;

import java.util.List;

public interface AuthorService {
    List<Author> saveAll(Iterable<Author> iterator);

    List<Author> findAll();

    List<Author> findByLastNameFragment(String lastNameFragment) throws ResourceNotFoundException;

    List<Author> findByFirstNameFragment(String firstNameFragment) throws ResourceNotFoundException;

    Author findById(Long id) throws ResourceNotFoundException;

    Author findAtRandom() throws ResourceNotFoundException;

    Author save(AuthorCreateDTO authorCreateDTO) throws DuplicateResourceException;

    Author updateAttributesOfAuthorFound(Author author, AuthorCreateDTO authorCreateDTO);

    Author updateAttributesThenSave(Long id, AuthorCreateDTO authorCreateDTO) throws ResourceNotFoundException;

    Author updateFoundAuthor(Author author, AuthorCreateDTO authorCreateDTO) throws WrongDataProvidedException;

    Author updateAuthorThenSave(Long anyLong, AuthorCreateDTO authorCreateDTO) throws ResourceNotFoundException, WrongDataProvidedException;

    void delete(Long id);
}
