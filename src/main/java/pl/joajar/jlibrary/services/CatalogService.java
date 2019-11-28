package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.dto.BookWithAuthorSetDTO;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;

import java.util.List;

public interface CatalogService {
    List<BookWithAuthorSetDTO> getBooksCatalog() throws ResourceNotFoundException;

    BookWithAuthorSetDTO findBookByBookId(Long id) throws ResourceNotFoundException;

    BookWithAuthorSetDTO findBookAtRandom() throws ResourceNotFoundException;
}
