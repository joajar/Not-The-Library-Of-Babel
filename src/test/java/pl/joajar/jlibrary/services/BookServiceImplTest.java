package pl.joajar.jlibrary.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.repository.BookRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    public void should_find_all_books() {
        //given
        final Book Java1 = Book.builder().id(1L).title("Java. Podstawy. Wydanie X").publicationDate(LocalDate.of(2016, 9, 26))
                .isbn("9788328324800").build();

        final Book Java2 = Book.builder().id(2L).title("Java. Techniki zaawansowane. Wydanie X").publicationDate(LocalDate.of(2017, 9, 28))
                .isbn("9788328334809").build();

        final Book Hibernate = Book.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13)).build();

        final List<Book> books;

        //when
        when(bookRepository.findAll()).thenReturn(Arrays.asList(Java1, Java2, Hibernate));
        books = bookService.findAll();

        //then
        assertEquals(3, books.size());
        assertEquals(Arrays.asList(Java1, Java2, Hibernate), books);
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }
}