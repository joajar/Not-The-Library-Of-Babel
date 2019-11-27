package pl.joajar.jlibrary.controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.joajar.jlibrary.dto.AuthorDTO;
import pl.joajar.jlibrary.dto.BookWithAuthorSetDTO;
import pl.joajar.jlibrary.exceptions.LibraryExceptionHandler;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.services.CatalogServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CatalogControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CatalogServiceImpl catalogService;

    @InjectMocks
    private CatalogController catalogController;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(catalogController)
                .setControllerAdvice(new LibraryExceptionHandler())
                .build();
    }

    @Test
    public void should_get_all_books_catalog() throws Exception {
        //given
        AuthorDTO HorstmannDTO = new AuthorDTO("Cay S.", "Horstmann", 1L);
        AuthorDTO BauerDTO = new AuthorDTO("Christian", "Bauer", 2L);
        AuthorDTO KingDTO = new AuthorDTO("Gavin", "King", 3L);
        AuthorDTO GregoryDTO = new AuthorDTO("Gary", "Gregory", 4L);

        BookWithAuthorSetDTO Java1 = BookWithAuthorSetDTO.builder().id(1L).title("Java. Podstawy. Wydanie X").publicationDate(LocalDate.of(2016, 9, 26))
                .isbn("9788328324800").authorSet(Collections.singleton(HorstmannDTO)).build();

        BookWithAuthorSetDTO Java2 = BookWithAuthorSetDTO.builder().id(2L).title("Java. Techniki zaawansowane. Wydanie X").publicationDate(LocalDate.of(2017, 9, 28))
                .isbn("9788328334809").authorSet(Collections.singleton(HorstmannDTO)).build();

        BookWithAuthorSetDTO Hibernate = BookWithAuthorSetDTO.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .authorSet(Stream.of(BauerDTO, KingDTO, GregoryDTO).collect(Collectors.toSet())).build();

        //when
        when(catalogService.getBooksCatalog()).thenReturn(Arrays.asList(Java1, Java2, Hibernate));

        //then
        mockMvc.perform(get("/v1/library/catalog"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(Matchers.is(1)))
                .andExpect(jsonPath("$[0].title").value(Matchers.is("Java. Podstawy. Wydanie X")))
                .andExpect(jsonPath("$[0].isbn").value(Matchers.is("9788328324800")))
                .andExpect(jsonPath("$[0].authorSet").exists())
                .andExpect(jsonPath("$[0].authorSet", hasSize(1)))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].id").value(Matchers.is(2)))
                .andExpect(jsonPath("$[1].title").value(Matchers.is("Java. Techniki zaawansowane. Wydanie X")))
                .andExpect(jsonPath("$[1].isbn").value(Matchers.is("9788328334809")))
                .andExpect(jsonPath("$[1].authorSet").exists())
                .andExpect(jsonPath("$[1].authorSet", hasSize(1)))
                .andExpect(jsonPath("$[2].id").exists())
                .andExpect(jsonPath("$[2].id").value(Matchers.is(3)))
                .andExpect(jsonPath("$[2].title").value(Matchers.is("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II")))
                .andExpect(jsonPath("$[2].isbn").value(Matchers.is("9788328327832")))
                .andExpect(jsonPath("$[2].authorSet").exists())
                .andExpect(jsonPath("$[2].authorSet", hasSize(3)))
        ;

        verify(catalogService, times(1)).getBooksCatalog();
        verifyNoMoreInteractions(catalogService);
    }

    @Test
    public void should_get_book_by_id() throws Exception {
        //given
        AuthorDTO BauerDTO = new AuthorDTO("Christian", "Bauer", 2L);
        AuthorDTO KingDTO = new AuthorDTO("Gavin", "King", 3L);
        AuthorDTO GregoryDTO = new AuthorDTO("Gary", "Gregory", 4L);

        BookWithAuthorSetDTO Hibernate = BookWithAuthorSetDTO.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .authorSet(Stream.of(BauerDTO, KingDTO, GregoryDTO).collect(Collectors.toSet())).build();

        //when
        when(catalogService.findBookByBookId(3L)).thenReturn(Hibernate);

        //then
        mockMvc.perform(get("/v1/library/catalog/{id}", 3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(Matchers.is(3)))
                .andExpect(jsonPath("$.title").value(Matchers.is("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II")))
                .andExpect(jsonPath("$.isbn").value(Matchers.is("9788328327832")))
                .andExpect(jsonPath("$.authorSet").exists())
                .andExpect(jsonPath("$.authorSet", hasSize(3)))
        //.andExpect(jsonPath("$.authorSet[0].lastName").value(Matchers.is("King")))
        ;

        verify(catalogService, times(1)).findBookByBookId(3L);
        verifyNoMoreInteractions(catalogService);
    }

    @Test
    public void should_get_book_at_random() throws Exception {
        //given
        AuthorDTO BauerDTO = new AuthorDTO("Christian", "Bauer", 2L);
        AuthorDTO KingDTO = new AuthorDTO("Gavin", "King", 3L);
        AuthorDTO GregoryDTO = new AuthorDTO("Gary", "Gregory", 4L);

        BookWithAuthorSetDTO Hibernate = BookWithAuthorSetDTO.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .authorSet(Stream.of(BauerDTO, KingDTO, GregoryDTO).collect(Collectors.toSet())).build();

        //when
        when(catalogService.findBookAtRandom()).thenReturn(Hibernate);

        //then
        mockMvc.perform(get("/v1/library/catalog/random").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(Matchers.is(3)))
                .andExpect(jsonPath("$.title").value(Matchers.is("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II")))
                .andExpect(jsonPath("$.isbn").value(Matchers.is("9788328327832")))
                .andExpect(jsonPath("$.authorSet").exists())
                .andExpect(jsonPath("$.authorSet", hasSize(3)));

        verify(catalogService, times(1)).findBookAtRandom();
        verifyNoMoreInteractions(catalogService);
    }

    @Test
    public void should_fail_while_getting_at_random_when_there_is_no_author_at_the_database() throws Exception {
        //when
        when(catalogService.findBookAtRandom()).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/catalog/random").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(catalogService, times(1)).findBookAtRandom();
        verifyNoMoreInteractions(catalogService);
    }
}
