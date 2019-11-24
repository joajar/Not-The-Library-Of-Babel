package pl.joajar.jlibrary.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joajar.jlibrary.dto.BookWithAuthorSetDTO;
import pl.joajar.jlibrary.services.CatalogService;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/library/catalog")
public class CatalogController {
    private CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(CatalogController.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<BookWithAuthorSetDTO>> getAllBooksCatalog() {
        LOG.info("CatalogController.getBooksCatalog: finding all books with their authors.");
        return new ResponseEntity<>(catalogService.getBooksCatalog(), HttpStatus.OK);
    }
}

