package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.exceptions.WrongDataProvidedException;

import java.util.List;

public interface BookService {
    long countBooks();

    void save(Book book);

    List<Book> findAll();

    Book findById(Long id) throws ResourceNotFoundException;

    List<Book> findByTitleFragment(String titleFragment) throws ResourceNotFoundException;

    List<Book> findByIsbnFragment(String isbnFragment) throws ResourceNotFoundException, WrongDataProvidedException;

    List<Book> findByPublicationYear(String publicationYear) throws ResourceNotFoundException, WrongDataProvidedException;
}
