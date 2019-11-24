package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
