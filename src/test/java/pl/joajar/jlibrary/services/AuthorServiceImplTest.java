package pl.joajar.jlibrary.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorDTO;
import pl.joajar.jlibrary.exceptions.DuplicateResourceException;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.repository.AuthorRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthorServiceImplTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    public void should_save_all_authors() {
        //given
        final Author Horstmann = Author.builder().firstName("Cay S.").lastName("Horstmann").build();
        final Author Bloch = Author.builder().firstName("Joshua").lastName("Bloch").build();
        final List<Author> authors;

        //when
        when(authorService.saveAll(Arrays.asList(Horstmann, Bloch))).thenReturn(Arrays.asList(Horstmann, Bloch));
        authors = authorService.saveAll(Arrays.asList(Horstmann, Bloch));

        //then
        assertEquals(2, authors.size());
        assertEquals(Arrays.asList(Horstmann, Bloch), authors);
        verify(authorRepository, times(1)).saveAll(anyList());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_find_all_authors() {
        //given
        final Author Horstmann = Author.builder().firstName("Cay S.").lastName("Horstmann").build();
        final Author Bloch = Author.builder().firstName("Joshua").lastName("Bloch").build();
        final List<Author> authors;

        //when
        when(authorRepository.findAll()).thenReturn(Arrays.asList(Horstmann, Bloch));
        authors = authorService.findAll();

        //then
        assertEquals(2, authors.size());
        assertEquals(Arrays.asList(Horstmann, Bloch), authors);
        verify(authorRepository, times(1)).findAll();
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_find_author_by_id() {
        //given
        final Author Bloch = Author.builder().firstName("Joshua").lastName("Bloch").build();
        final Author author;

        //when
        when(authorRepository.findById(1L)).thenReturn(Optional.ofNullable(Bloch));
        author = authorService.findById(1L);

        //then
        assertNotNull(author);
        assertEquals(Bloch, author);
        verify(authorRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_not_find_nonexistent_author_by_id() {
        //given
        final Author author;

        //when
        when(authorRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);
        author = authorService.findById(1L);

        //then
        assertNull(author);
        verify(authorRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_save_new_author() {
        //given
        Author Walls = Author.builder().id(6L).firstName("Craig").lastName("Walls").build();
        AuthorDTO WallsDTO = AuthorDTO.builder().firstName("Craig").lastName("Walls").build();
        final Author author;

        //when
        when(authorRepository.save(any(Author.class))).thenReturn(Walls);
        author = authorService.save(WallsDTO);

        //then
        assertNotNull(author);
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorRepository, times(1)).findByFirstNameAndLastName(anyString(), anyString());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test(expected = DuplicateResourceException.class)
    public void should_fail_while_saving_author_that_exists() {
        //given
        AuthorDTO WallsDTO = AuthorDTO.builder().firstName("Craig").lastName("Walls").build();
        final Author author;

        //when
        when(authorRepository.save(any(Author.class))).thenThrow(DuplicateResourceException.class);
        author = authorService.save(WallsDTO);

        //then
        assertNull(author);
        verify(authorRepository, times(1)).findByFirstNameAndLastName(anyString(), anyString());
        verifyNoMoreInteractions(authorRepository);
    }

}
