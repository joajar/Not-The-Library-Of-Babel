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
import pl.joajar.jlibrary.services.BookServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;

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
}
