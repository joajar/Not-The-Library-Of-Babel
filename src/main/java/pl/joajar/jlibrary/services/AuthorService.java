package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    List<Author> saveAll(Iterable<Author> iterator);
}
