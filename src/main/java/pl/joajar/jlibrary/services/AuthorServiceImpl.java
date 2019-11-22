package pl.joajar.jlibrary.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorDTO;
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
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public Author findAtRandom() throws ResourceNotFoundException {
        long authorsListSize = authorRepository.count();
        LOG.info("AuthorServiceImpl.findAtRandom: finding one author at random, out of {} authors", authorsListSize);
        if (authorsListSize == 0) throw new ResourceNotFoundException("There is no author at the database!");

        Long id = (long) (Math.random() * authorsListSize) + 1;

        return findById(id);
    }

    @Override
    public Author save(AuthorDTO authorDTO) throws DuplicateResourceException {
        if (authorRepository.findByFirstNameAndLastName(authorDTO.getFirstName(), authorDTO.getLastName()).isPresent())
        {
            throw new DuplicateResourceException(authorDTO.getFirstName(), authorDTO.getLastName());
        }
        Author author = authorRepository.save(new Author(authorDTO.getFirstName(), authorDTO.getLastName()));
        LOG.info("AuthorServiceImpl.save: saving under id = {} the author with the first name {} and the last name {}.",
                author.getId(), author.getFirstName(), author.getLastName());
        return author;
    }

    @Override
    public Author updateAttributesOfAuthorFound(Author author, AuthorDTO authorDTO) {

        author.setFirstName(
                Optional.ofNullable(authorDTO.getFirstName())
                        .filter(firstName -> firstName.length() > 0)
                        .orElse(author.getFirstName())
        );

        author.setLastName(
                Optional.ofNullable(authorDTO.getLastName())
                        .filter(lastName -> lastName.length() > 0)
                        .orElse(author.getLastName())
        );

        LOG.info("AuthorServiceImpl.updateAttributesOfFoundAuthor: updating attributes of the given author.");
        return author;
    }

    @Override
    public Author updateAttributesThenSave(Long id, AuthorDTO authorDTO) throws ResourceNotFoundException {

        Author author = findById(id);

        return authorRepository.save(updateAttributesOfAuthorFound(author, authorDTO));
    }

    @Override
    public Author updateFoundAuthor(Author author, AuthorDTO authorDTO) throws NullDataProvidedException {

        author.setFirstName(
                Optional.ofNullable(authorDTO.getFirstName())
                        .filter(firstName -> firstName.length() > 0)
                        .orElseThrow(NullDataProvidedException::new)
        );

        author.setLastName(
                Optional.ofNullable(authorDTO.getLastName())
                        .filter(lastName -> lastName.length() > 0)
                        .orElseThrow(NullDataProvidedException::new)
        );

        LOG.info("AuthorServiceImpl.updateFoundAuthor: the given author is updated.");
        return author;
    }

    @Override
    public Author updateAuthorThenSave(Long id, AuthorDTO authorDTO) throws ResourceNotFoundException, NullDataProvidedException {

        Author author = findById(id);

        return authorRepository.save(updateFoundAuthor(author, authorDTO));
    }

    @Override
    public void delete(Long id) {
        if (authorRepository.findById(id).isPresent())
            authorRepository.delete(authorRepository.findById(id).get());
        LOG.info("AuthorServiceImpl.delete: deleting the author with id = {} provided it exists.", id);
    }


}
