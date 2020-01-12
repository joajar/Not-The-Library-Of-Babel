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
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.exceptions.LibraryExceptionHandler;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.exceptions.WrongDataProvidedException;
import pl.joajar.jlibrary.services.BookServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private BookController bookController;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new LibraryExceptionHandler())
                .build();
    }

    @Test
    public void should_get_all_books() throws Exception {
        //given
        final Book Java1 = Book.builder().id(1L).title("Java. Podstawy. Wydanie X").publicationDate(LocalDate.of(2016, 9, 26))
                .isbn("9788328324800").build();

        final Book Java2 = Book.builder().id(2L).title("Java. Techniki zaawansowane. Wydanie X").publicationDate(LocalDate.of(2017, 9, 28))
                .isbn("9788328334809").build();

        final Book Hibernate = Book.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13)).build();

        //when
        when(bookService.findAll()).thenReturn(Arrays.asList(Java1, Java2, Hibernate));

        //then
        mockMvc.perform(get("/v1/library/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(Matchers.is(1)))
                .andExpect(jsonPath("$[0].title").value(Matchers.is("Java. Podstawy. Wydanie X")))
                .andExpect(jsonPath("$[0].isbn").value(Matchers.is("9788328324800")))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].id").value(Matchers.is(2)))
                .andExpect(jsonPath("$[1].title").value(Matchers.is("Java. Techniki zaawansowane. Wydanie X")))
                .andExpect(jsonPath("$[1].isbn").value(Matchers.is("9788328334809")))
                .andExpect(jsonPath("$[2].id").exists())
                .andExpect(jsonPath("$[2].id").value(Matchers.is(3)))
                .andExpect(jsonPath("$[2].title").value(Matchers.is("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II")))
                .andExpect(jsonPath("$[2].isbn").value(Matchers.is("9788328327832")))
        ;
    }

    @Test
    public void should_get_book_by_title_fragment() throws Exception {
        //given
        final Book Java1 = Book.builder().id(1L).title("Java. Podstawy. Wydanie X").publicationDate(LocalDate.of(2016, 9, 26))
                .isbn("9788328324800").build();

        final Book Java2 = Book.builder().id(2L).title("Java. Techniki zaawansowane. Wydanie X").publicationDate(LocalDate.of(2017, 9, 28))
                .isbn("9788328334809").build();

        //when
        when(bookService.findByTitleFragment("Wydanie X")).thenReturn(Arrays.asList(Java1, Java2));

        //then
        mockMvc.perform(get("/v1/library/books/title/{titleFragment}", "Wydanie X").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(Matchers.is(1)))
                .andExpect(jsonPath("$[0].title").value(Matchers.is("Java. Podstawy. Wydanie X")))
                .andExpect(jsonPath("$[0].isbn").value(Matchers.is("9788328324800")))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].id").value(Matchers.is(2)))
                .andExpect(jsonPath("$[1].title").value(Matchers.is("Java. Techniki zaawansowane. Wydanie X")))
                .andExpect(jsonPath("$[1].isbn").value(Matchers.is("9788328334809")));

        verify(bookService, times(1)).findByTitleFragment(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_fail_while_getting_books_list_with_title_fragment_nonexistent_in_db() throws Exception {
        //when
        when(bookService.findByTitleFragment("python")).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/books/title/{titleFragment}", "python").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findByTitleFragment(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_get_book_by_isbn_fragment() throws Exception {
        //given
        final Book Java1 = Book.builder().id(1L).title("Java. Podstawy. Wydanie X").publicationDate(LocalDate.of(2016, 9, 26))
                .isbn("9788328324800").build();

        //when
        when(bookService.findByIsbnFragment("00")).thenReturn(Collections.singletonList(Java1));

        //then
        mockMvc.perform(get("/v1/library/books/isbn/{isbnFragment}", "00").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(Matchers.is(1)))
                .andExpect(jsonPath("$[0].title").value(Matchers.is("Java. Podstawy. Wydanie X")))
                .andExpect(jsonPath("$[0].isbn").value(Matchers.is("9788328324800")))
        ;

        verify(bookService, times(1)).findByIsbnFragment(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_fail_while_getting_books_list_with_isbn_nonnumerical_fragment() throws Exception {
        //when
        when(bookService.findByIsbnFragment("python")).thenThrow(WrongDataProvidedException.class);

        //then
        mockMvc.perform(get("/v1/library/books/isbn/{isbnFragment}", "python").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable());

        verify(bookService, times(1)).findByIsbnFragment(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_fail_while_getting_books_list_with_isbn_numerical_fragment_nonexistent_in_db() throws Exception {
        //when
        when(bookService.findByIsbnFragment("0000")).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/books/isbn/{isbnFragment}", "0000").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findByIsbnFragment(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_get_book_by_publication_year() throws Exception {
        //given
        final Book Spring5EnEd = Book.builder().id(6L).title("Spring in Action, Fifth Edition").isbn("9781617294945")
                .publicationDate(LocalDate.of(2019, 10, 1)).build();

        //when
        when(bookService.findByPublicationYear("19")).thenReturn(Collections.singletonList(Spring5EnEd));

        //then
        mockMvc.perform(get("/v1/library/books/publicationyear/{publicationYear}", "19").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(Matchers.is(6)))
                .andExpect(jsonPath("$[0].title").value(Matchers.is("Spring in Action, Fifth Edition")))
                .andExpect(jsonPath("$[0].isbn").value(Matchers.is("9781617294945")))
        ;

        verify(bookService, times(1)).findByPublicationYear(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_fail_while_getting_list_of_books_published_in_given_year_nonexistent_in_db() throws Exception {
        //when
        when(bookService.findByPublicationYear("11")).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/books/publicationyear/{publicationYear}", "11").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findByPublicationYear(anyString());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void should_fail_while_getting_list_of_books_published_in_given_year_with_nonnumerical_year() throws Exception {
        //when
        when(bookService.findByPublicationYear("python")).thenThrow(WrongDataProvidedException.class);

        //then
        mockMvc.perform(get("/v1/library/books/publicationyear/{publicationYear}", "python").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotAcceptable());

        verify(bookService, times(1)).findByPublicationYear(anyString());
        verifyNoMoreInteractions(bookService);
    }
}
