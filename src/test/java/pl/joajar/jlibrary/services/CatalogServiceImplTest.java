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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

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
    }
}
