package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.dto.BookWithAuthorSetDTO;

import java.util.List;

public interface CatalogService {
    List<BookWithAuthorSetDTO> getBooksCatalog();
}
