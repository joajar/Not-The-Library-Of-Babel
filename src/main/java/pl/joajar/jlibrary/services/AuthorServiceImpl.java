package pl.joajar.jlibrary.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private static final Logger LOG = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public List<Author> findAll() {
        LOG.info("AuthorServiceImpl.findAll(): finding all authors from the library.");
        return authorRepository.findAll();
    }

    @Override
    public List<Author> saveAll(Iterable<Author> iterator) {
        LOG.info("AuthorServiceImpl.saveAll(iterator): saving all authors from the iterator.");
        return authorRepository.saveAll(iterator);
    }

    @Override
    public Author findById(Long id) throws ResourceNotFoundException {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
