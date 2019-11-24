package pl.joajar.jlibrary.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.domain.Relation;
import pl.joajar.jlibrary.dto.AuthorDTO;
import pl.joajar.jlibrary.dto.BookWithAuthorSetDTO;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CatalogServiceImpl implements CatalogService {
    private BookService bookService;
    private RelationService relationService;

    private static final Logger LOG = LoggerFactory.getLogger(CatalogServiceImpl.class);

    @Autowired
    public CatalogServiceImpl(BookService bookService, RelationService relationService) {
        this.bookService = bookService;
        this.relationService = relationService;
    }
    @Override
    public List<BookWithAuthorSetDTO> getBooksCatalog() {

        if (bookService.findAll() == null || bookService.findAll().size() == 0) {
            LOG.warn("Database error: there is no books!");
            throw new ResourceNotFoundException("Database error: there is no books!");
        }

        List<BookWithAuthorSetDTO> bookWithAuthorSetDTOList = new ArrayList<>();

        for (Book book : bookService.findAll()) {

            BookWithAuthorSetDTO bookWithAuthorSetDTO = new BookWithAuthorSetDTO();

            bookWithAuthorSetDTO.setId(book.getId());
            bookWithAuthorSetDTO.setTitle(book.getTitle());
            bookWithAuthorSetDTO.setPublicationDate(book.getPublicationDate());
            bookWithAuthorSetDTO.setIsbn(book.getIsbn());

            if (relationService.findRelationByBookId(book.getId()) == null || relationService.findRelationByBookId(book.getId()).size() == 0) {
                LOG.warn("Database error: there is no authors for the book with id = {}.", book.getId());
                throw new ResourceNotFoundException("Database error: there is no authors for the book with id = " + book.getId() + ".");
            }

            Set<AuthorDTO> authorSet = new HashSet<>();

            for (Relation relation : relationService.findRelationByBookId(book.getId())) {
                Author author = relation.getAuthor();
                AuthorDTO authorDTO = new AuthorDTO(author.getFirstName(), author.getLastName(), author.getId());
                authorSet.add(authorDTO);
            }

            bookWithAuthorSetDTO.setAuthorSet(authorSet);

            bookWithAuthorSetDTOList.add(bookWithAuthorSetDTO);
        }

        return bookWithAuthorSetDTOList;
    }
}

