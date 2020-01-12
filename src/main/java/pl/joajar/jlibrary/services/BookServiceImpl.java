package pl.joajar.jlibrary.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.exceptions.WrongDataProvidedException;
import pl.joajar.jlibrary.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BookServiceImpl implements BookService {
    private static final Logger LOG = LoggerFactory.getLogger(BookServiceImpl.class);

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public long countBooks() {
        return bookRepository.count();
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        LOG.info("BookServiceImpl.findAll: finding all books from the library.");
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) throws ResourceNotFoundException {
        LOG.info("BookServiceImpl.findById: finding the book with id = {} provided it exists.", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public List<Book> findByTitleFragment(String titleFragment) throws ResourceNotFoundException {
        LOG.info("BookServiceImpl.findByTitleFragment: finding the book with its title containing {} provided it exists.", titleFragment);

        List<Book> bookList = bookRepository.findByTitleIgnoringCaseContainingOrderById(titleFragment);

        if (bookList == null) {
            LOG.info("BookServiceImpl.findByTitleFragment: there is null instead of the list with books containing {} in their titles.", titleFragment);
            throw new ResourceNotFoundException("BookServiceImpl.findByTitleFragment: found no book satisfying given condition.");
        }

        if (bookList.size() == 0) {
            LOG.info("BookServiceImpl.findByTitleFragment: the list with books containing {} in their titles is empty.", titleFragment);
            throw new ResourceNotFoundException("BookServiceImpl.findByTitleFragment: found no book satisfying given condition.");
        }

        return bookList;
    }

    @Override
    public List<Book> findByIsbnFragment(String isbnFragment) throws ResourceNotFoundException, WrongDataProvidedException {

/*        if (isbnFragment == null)
            throw new ResourceNotFoundException("BookServiceImpl.findByIsbnFragment: found no book satisfying given condition.");*/

        if (!Pattern.compile("^[\\d]{1,13}$").matcher(isbnFragment).find())
            throw new WrongDataProvidedException("BookServiceImpl.findByIsbnFragment: wrong isbn fragment given.");

        LOG.info("BookServiceImpl.findByIsbnFragment: finding the book with its isbn containing {} provided it exists.", isbnFragment);

        List<Book> bookList = bookRepository.findByIsbnContaining(isbnFragment);

        if (bookList == null) {
            LOG.info("BookServiceImpl.findByIsbnFragment: there is null instead of the list with books containing {} in their isbn.", isbnFragment);
            throw new ResourceNotFoundException("BookServiceImpl.findByIsbnFragment: found no book satisfying given condition.");
        }

        if (bookList.size() == 0) {
            LOG.info("BookServiceImpl.findByIsbnFragment: the list with books containing {} in their isbn is empty.", isbnFragment);
            throw new ResourceNotFoundException("BookServiceImpl.findByIsbnFragment: found no book satisfying given condition.");
        }

        return bookList;
    }

    @Override
    public List<Book> findByPublicationYear(String publicationYear) throws ResourceNotFoundException, WrongDataProvidedException {

        if (!Pattern.compile("^[\\d]{1,4}$").matcher(publicationYear).find())
            throw new WrongDataProvidedException("BookServiceImpl.findByPublicationYear: obtained data is not a year number.");

        LOG.info("BookServiceImpl.findByPublicationYear: finding the book with its isbn containing {} provided it exists.", publicationYear);

        List<Book> bookList;

        if (Pattern.compile("^[\\d]{2}$").matcher(publicationYear).find() && (Integer.parseInt(publicationYear) >= 10))
        {
            bookList = bookRepository.findByPublicationDateBetweenOrderById(
                    LocalDate.of(Integer.parseInt("20"+publicationYear), 1, 1),
                    LocalDate.of(Integer.parseInt("20"+publicationYear), 12, 31)
            );
        }
        else if (Pattern.compile("^[\\d]{4}$").matcher(publicationYear).find() && (Integer.parseInt(publicationYear) >= 2010))
        {
            bookList = bookRepository.findByPublicationDateBetweenOrderById(
                    LocalDate.of(Integer.parseInt(publicationYear), 1, 1),
                    LocalDate.of(Integer.parseInt(publicationYear), 12, 31)
            );
        }
        else
            throw new WrongDataProvidedException("BookServiceImpl.findByPublicationYear: obtained data is not a valid year number.");

        if (bookList == null) {
            LOG.info("BookServiceImpl.findByPublicationYear: there is null instead of the list with books published in year {}.", publicationYear);
            throw new ResourceNotFoundException("BookServiceImpl.findByIsbnFragment: found no book satisfying given condition.");
        }

        if (bookList.size() == 0) {
            LOG.info("BookServiceImpl.findByPublicationYear: the list with books published in year {} is empty.", publicationYear);
            throw new ResourceNotFoundException("BookServiceImpl.findByIsbnFragment: found no book satisfying given condition.");
        }

        return bookList;
    }
}
