package pl.joajar.jlibrary.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.exceptions.WrongDataProvidedException;
import pl.joajar.jlibrary.repository.BookRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

    @Test
    public void should_find_book_by_id() {
        //given
        final Book Hibernate = Book.builder().id(1L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II")
                .isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .build();
        final Book book;

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(Hibernate));
        book = bookService.findById(1L);

        //then
        assertNotNull(book);
        assertEquals(Hibernate, book);
        verify(bookRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_nonexistent_book_by_id() {
        //given
        final Book book;

        //when
        when(bookRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);
        book = bookService.findById(1L);

        //then
        assertNull(book);
    }

    @Test
    public void should_find_books_published_in_given_year() {
        //given
        final Book Spring5EnEd = Book.builder().id(6L).title("Spring in Action, Fifth Edition").isbn("9781617294945")
                .publicationDate(LocalDate.of(2019, 10, 1)).build();

        final List<Book> books;

        //when
        when(bookRepository.findByPublicationDateBetweenOrderById(any(), any())).thenReturn(Collections.singletonList(Spring5EnEd));
        books = bookService.findByPublicationYear("19");

        //then
        assertEquals(1, books.size());
        assertEquals(Collections.singletonList(Spring5EnEd), books);
        verify(bookRepository, times(1)).findByPublicationDateBetweenOrderById(any(), any());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_nonexistent_book_by_publishing_year() {
        //given
        final List<Book> bookList;

        //when
        when(bookRepository.findByPublicationDateBetweenOrderById(any(), any())).thenReturn(Collections.emptyList());
        bookList = bookService.findByPublicationYear("11");

        //then
        assertNull(bookList);
        verify(bookRepository, times(1)).findByPublicationDateBetweenOrderById(any(), any());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test(expected = WrongDataProvidedException.class)
    public void should_fail_while_finding_book_by_publishing_year_with_providing_nonnumerical_year_value() {
        //given
        final List<Book> bookList;

        //when
        bookList = bookService.findByPublicationYear("aa");

        //then
        assertNull(bookList);
        verify(bookRepository, times(1)).findByPublicationDateBetweenOrderById(any(), any());
        verifyNoMoreInteractions(bookRepository);
    }
}
