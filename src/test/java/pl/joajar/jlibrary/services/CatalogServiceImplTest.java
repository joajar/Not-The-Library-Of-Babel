package pl.joajar.jlibrary.services;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.domain.Book;
import pl.joajar.jlibrary.domain.Relation;
import pl.joajar.jlibrary.dto.AuthorDTO;
import pl.joajar.jlibrary.dto.BookWithAuthorSetDTO;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CatalogServiceImplTest {
    @Mock
    private BookService bookService;

    @Mock
    private RelationService relationService;

    @InjectMocks
    private CatalogServiceImpl catalogService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        catalogService = new CatalogServiceImpl(bookService, relationService);
    }

    @Test
    public void should_get_books_catalog() throws Exception {
        //given
        Author Horstmann = Author.builder().id(1L).firstName("Cay S.").lastName("Horstmann").build();
        Relation HorstmannRelation1 = Relation.builder().id(1L).author(Horstmann).build();
        Relation HorstmannRelation2 = Relation.builder().id(2L).author(Horstmann).build();

        Author Bauer = Author.builder().id(2L).firstName("Christian").lastName("Bauer").build();
        Relation BauerRelation = Relation.builder().id(3L).author(Bauer).build();

        Author King = Author.builder().id(3L).firstName("Gavin").lastName("King").build();
        Relation KingRelation = Relation.builder().id(4L).author(King).build();

        Author Gregory = Author.builder().id(4L).firstName("Gary").lastName("Gregory").build();
        Relation GregoryRelation = Relation.builder().id(5L).author(Gregory).build();

        Book Java1 = Book.builder().id(1L).title("Java. Podstawy. Wydanie X").publicationDate(LocalDate.of(2016, 9, 26))
                .isbn("9788328324800").relationSet(Collections.singleton(HorstmannRelation1)).build();

        Book Java2 = Book.builder().id(2L).title("Java. Techniki zaawansowane. Wydanie X").publicationDate(LocalDate.of(2017, 9, 28))
                .isbn("9788328334809").relationSet(Collections.singleton(HorstmannRelation2)).build();

        Book Hibernate = Book.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .relationSet(Stream.of(BauerRelation, KingRelation, GregoryRelation).collect(Collectors.toSet())).build();

        List<BookWithAuthorSetDTO> bookWithAuthorSetDTOList;

        //when
        when(bookService.findAll()).thenReturn(Arrays.asList(Java1, Java2, Hibernate));
        when(relationService.findRelationByBookId(1L)).thenReturn(Collections.singletonList(HorstmannRelation1));
        when(relationService.findRelationByBookId(2L)).thenReturn(Collections.singletonList(HorstmannRelation2));
        when(relationService.findRelationByBookId(3L)).thenReturn(Arrays.asList(BauerRelation, KingRelation, GregoryRelation));

        bookWithAuthorSetDTOList = catalogService.getBooksCatalog();

        //then
        assertNotNull(bookWithAuthorSetDTOList);
        assertEquals(3, bookWithAuthorSetDTOList.size());

        assertEquals("Java. Podstawy. Wydanie X", bookWithAuthorSetDTOList.get(0).getTitle());
        assertEquals(1, bookWithAuthorSetDTOList.get(0).getAuthorSet().size());
        assertEquals("Horstmann", bookWithAuthorSetDTOList.get(0).getAuthorSet().stream().findFirst().orElseThrow(Exception::new).getLastName());

        assertEquals("Java. Techniki zaawansowane. Wydanie X", bookWithAuthorSetDTOList.get(1).getTitle());
        assertEquals("Horstmann", bookWithAuthorSetDTOList.get(1).getAuthorSet().stream().findAny().orElseThrow(Exception::new).getLastName());

        assertEquals("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II", bookWithAuthorSetDTOList.get(2).getTitle());
        assertEquals(3, bookWithAuthorSetDTOList.get(2).getAuthorSet().size());
        assertTrue(CollectionUtils.isEqualCollection(
                Stream.of("Gregory", "Bauer", "King").collect(Collectors.toSet()),
                bookWithAuthorSetDTOList.get(2).getAuthorSet().stream().map(AuthorDTO::getLastName).collect(Collectors.toSet())
        ));

        verify(bookService, times(3)).findAll();
        verifyNoMoreInteractions(bookService);
        verify(relationService, times(9)).findRelationByBookId(anyLong());
        verifyNoMoreInteractions(relationService);
    }

    @Test
    public void should_find_book_by_id() {
        //given
        final Author Bauer = Author.builder().id(2L).firstName("Christian").lastName("Bauer").build();
        final Relation BauerRelation = Relation.builder().id(3L).author(Bauer).build();

        final Author King = Author.builder().id(3L).firstName("Gavin").lastName("King").build();
        final Relation KingRelation = Relation.builder().id(4L).author(King).build();

        final Author Gregory = Author.builder().id(4L).firstName("Gary").lastName("Gregory").build();
        final Relation GregoryRelation = Relation.builder().id(5L).author(Gregory).build();

        final Book Hibernate = Book.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .relationSet(Stream.of(BauerRelation, KingRelation, GregoryRelation).collect(Collectors.toSet())).build();

        final BookWithAuthorSetDTO bookWithAuthorSetDTO;

        //when
        when(bookService.findById(3L)).thenReturn(Hibernate);
        when(relationService.findRelationByBookId(3L)).thenReturn(Arrays.asList(BauerRelation, KingRelation, GregoryRelation));

        bookWithAuthorSetDTO = catalogService.findBookByBookId(3L);

        //then
        assertNotNull(bookWithAuthorSetDTO);
        assertEquals("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II", bookWithAuthorSetDTO.getTitle());
        assertEquals(3, bookWithAuthorSetDTO.getAuthorSet().size());
        assertTrue(CollectionUtils.isEqualCollection(
                Stream.of("Gregory", "Bauer", "King").collect(Collectors.toSet()),
                bookWithAuthorSetDTO.getAuthorSet().stream().map(AuthorDTO::getLastName).collect(Collectors.toSet())
        ));

        verify(bookService, times(1)).findById(3L);
        verifyNoMoreInteractions(bookService);
        verify(relationService, times(3)).findRelationByBookId(3L);
        verifyNoMoreInteractions(relationService);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_nonexistent_book_by_id() {
        //given
        final BookWithAuthorSetDTO bookWithAuthorSetDTO;

        //when
        when(bookService.findById(1L)).thenThrow(ResourceNotFoundException.class);
        bookWithAuthorSetDTO = catalogService.findBookByBookId(1L);

        //then
        assertNull(bookWithAuthorSetDTO);
        verify(bookService, times(1)).findById(1L);
        verifyNoMoreInteractions(bookService);
        verifyNoInteractions(relationService);
    }

    @Test
    public void should_find_book_at_random() {
        //given
        final Author Bauer = Author.builder().id(2L).firstName("Christian").lastName("Bauer").build();
        final Relation BauerRelation = Relation.builder().id(3L).author(Bauer).build();

        final Author King = Author.builder().id(3L).firstName("Gavin").lastName("King").build();
        final Relation KingRelation = Relation.builder().id(4L).author(King).build();

        final Author Gregory = Author.builder().id(4L).firstName("Gary").lastName("Gregory").build();
        final Relation GregoryRelation = Relation.builder().id(5L).author(Gregory).build();

        final Book Hibernate = Book.builder().id(3L).title("Java Persistence. Programowanie aplikacji bazodanowych w Hibernate. Wydanie II").isbn("9788328327832")
                .publicationDate(LocalDate.of(2016, 12, 13))
                .relationSet(Stream.of(BauerRelation, KingRelation, GregoryRelation).collect(Collectors.toSet())).build();

        final BookWithAuthorSetDTO bookWithAuthorSetDTO;

        //when
        when(bookService.countBooks()).thenReturn(2L);
        when(bookService.findById(anyLong())).thenReturn(Hibernate);
        when(relationService.findRelationByBookId(anyLong())).thenReturn(Arrays.asList(BauerRelation, KingRelation, GregoryRelation));
        bookWithAuthorSetDTO = catalogService.findBookAtRandom();

        //then
        assertNotNull(bookWithAuthorSetDTO);
        assertTrue(CollectionUtils.isEqualCollection(
                Stream.of("Gregory", "Bauer", "King").collect(Collectors.toSet()),
                bookWithAuthorSetDTO.getAuthorSet().stream().map(AuthorDTO::getLastName).collect(Collectors.toSet())
        ));
        verify(bookService, times(1)).countBooks();
        verify(bookService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(bookService);
        verify(relationService, times(3)).findRelationByBookId(anyLong());
        verifyNoMoreInteractions(relationService);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_book_at_random_when_there_is_no_book() {
        //given
        final BookWithAuthorSetDTO bookWithAuthorSetDTO;

        //when
        when(bookService.countBooks()).thenReturn(0L);
        bookWithAuthorSetDTO = catalogService.findBookAtRandom();

        //then
        assertNull(bookWithAuthorSetDTO);
        verify(bookService, times(1)).countBooks();
        verifyNoMoreInteractions(bookService);
    }
}
