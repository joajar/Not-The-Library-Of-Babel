package pl.joajar.jlibrary.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorCreateDTO;
import pl.joajar.jlibrary.exceptions.DuplicateResourceException;
import pl.joajar.jlibrary.exceptions.NullDataProvidedException;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
        LOG.info("AuthorServiceImpl.findAll: finding all authors from the library.");
        return authorRepository.findAll();
    }

    @Override
    public List<Author> saveAll(Iterable<Author> iterator) {
        LOG.info("AuthorServiceImpl.saveAll: saving all authors from the iterator.");
        return authorRepository.saveAll(iterator);
    }

    @Override
    public Author findById(Long id) throws ResourceNotFoundException {
        LOG.info("AuthorServiceImpl.findById: finding the author with id = {} provided it exists.", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public Author findAtRandom() throws ResourceNotFoundException {
        long authorsListSize = authorRepository.count();

        LOG.info("AuthorServiceImpl.findAtRandom: finding one author at random, out of {} authors", authorsListSize);

        if (authorsListSize == 0) throw new ResourceNotFoundException("There is no author at the database!");

        return findById((long) (Math.random() * authorsListSize) + 1);
    }

    @Override
    public Author save(AuthorCreateDTO authorCreateDTO) throws DuplicateResourceException {
        if (authorRepository.findByFirstNameAndLastName(authorCreateDTO.getFirstName(), authorCreateDTO.getLastName()).isPresent())
        {
            throw new DuplicateResourceException(authorCreateDTO.getFirstName(), authorCreateDTO.getLastName());
        }
        Author author = authorRepository.save(new Author(authorCreateDTO.getFirstName(), authorCreateDTO.getLastName()));
        LOG.info("AuthorServiceImpl.save: saving under id = {} the author with the first name {} and the last name {}.",
                author.getId(), author.getFirstName(), author.getLastName());
        return author;
    }

    @Override
    public Author updateAttributesOfAuthorFound(Author author, AuthorCreateDTO authorCreateDTO) {

        author.setFirstName(
                Optional.ofNullable(authorCreateDTO.getFirstName())
                        .filter(firstName -> firstName.length() > 0)
                        .orElse(author.getFirstName())
        );

        author.setLastName(
                Optional.ofNullable(authorCreateDTO.getLastName())
                        .filter(lastName -> lastName.length() > 0)
                        .orElse(author.getLastName())
        );

        LOG.info("AuthorServiceImpl.updateAttributesOfFoundAuthor: updating attributes of the given author.");
        return author;
    }

    @Override
    public Author updateAttributesThenSave(Long id, AuthorCreateDTO authorCreateDTO) throws ResourceNotFoundException {

        Author author = findById(id);

        return authorRepository.save(updateAttributesOfAuthorFound(author, authorCreateDTO));
    }

    @Override
    public Author updateFoundAuthor(Author author, AuthorCreateDTO authorCreateDTO) throws NullDataProvidedException {

        author.setFirstName(
                Optional.ofNullable(authorCreateDTO.getFirstName())
                        .filter(firstName -> firstName.length() > 0)
                        .orElseThrow(NullDataProvidedException::new)
        );

        author.setLastName(
                Optional.ofNullable(authorCreateDTO.getLastName())
                        .filter(lastName -> lastName.length() > 0)
                        .orElseThrow(NullDataProvidedException::new)
        );

        LOG.info("AuthorServiceImpl.updateFoundAuthor: the given author is updated.");
        return author;
    }

    @Override
    public Author updateAuthorThenSave(Long id, AuthorCreateDTO authorCreateDTO) throws ResourceNotFoundException, NullDataProvidedException {

        Author author = findById(id);

        return authorRepository.save(updateFoundAuthor(author, authorCreateDTO));
    }

    @Override
    public void delete(Long id) {
        if (authorRepository.findById(id).isPresent())
            authorRepository.delete(authorRepository.findById(id).get());
        LOG.info("AuthorServiceImpl.delete: deleting the author with id = {} provided it exists.", id);
    }
}