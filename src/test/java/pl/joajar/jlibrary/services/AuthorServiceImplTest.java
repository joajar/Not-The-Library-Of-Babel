package pl.joajar.jlibrary.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorCreateDTO;
import pl.joajar.jlibrary.exceptions.DuplicateResourceException;
import pl.joajar.jlibrary.exceptions.WrongDataProvidedException;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.repository.AuthorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
        when(authorRepository.saveAll(Arrays.asList(Horstmann, Bloch))).thenReturn(Arrays.asList(Horstmann, Bloch));
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
    public void should_find_authors_with_given_last_name_fragment() {
        //given
        final Author Horstmann = Author.builder().id(1L).firstName("Cay S.").lastName("Horstmann").build();
        final Author Gregory = Author.builder().id(5L).firstName("Gary").lastName("Gregory").build();
        final List<Author> authors;

        //when
        when(authorRepository.findByLastNameIgnoringCaseContainingOrderById("or")).thenReturn(Arrays.asList(Horstmann, Gregory));
        authors = authorService.findByLastNameFragment("or");

        //then
        assertEquals(2, authors.size());
        assertEquals(Arrays.asList(Horstmann, Gregory), authors);
        verify(authorRepository, times(1)).findByLastNameIgnoringCaseContainingOrderById(anyString());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_nonexistent_author_by_last_name_fragment() {
        //given
        final List<Author> authorList;

        //when
        when(authorRepository.findByLastNameIgnoringCaseContainingOrderById(anyString())).thenThrow(ResourceNotFoundException.class);
        authorList = authorService.findByLastNameFragment("wezyk");

        //then
        assertNull(authorList);
        verify(authorRepository, times(1)).findByLastNameIgnoringCaseContainingOrderById(anyString());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_find_authors_with_given_first_name_fragment() {
        //given
        final Author Bauer = Author.builder().id(3L).firstName("Christian").lastName("Bauer").build();
        final Author Mehta = Author.builder().id(7L).firstName("Bhakti").lastName("Mehta").build();
        final List<Author> authors;

        //when
        when(authorRepository.findByFirstNameIgnoringCaseContainingOrderById("ti")).thenReturn(Arrays.asList(Bauer, Mehta));
        authors = authorService.findByFirstNameFragment("ti");

        //then
        assertEquals(2, authors.size());
        assertEquals(Arrays.asList(Bauer, Mehta), authors);
        verify(authorRepository, times(1)).findByFirstNameIgnoringCaseContainingOrderById(anyString());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_nonexistent_author_by_first_name_fragment() {
        //given
        final List<Author> authorList;

        //when
        when(authorRepository.findByFirstNameIgnoringCaseContainingOrderById(anyString())).thenThrow(ResourceNotFoundException.class);
        authorList = authorService.findByFirstNameFragment("wezyk");

        //then
        assertNull(authorList);
        verify(authorRepository, times(1)).findByFirstNameIgnoringCaseContainingOrderById(anyString());
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
    public void should_fail_while_finding_nonexistent_author_by_id() {
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
    public void should_find_author_at_random() {
        //given
        final Author Bloch = Author.builder().firstName("Joshua").lastName("Bloch").build();
        final Author author;

        //when
        when(authorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(Bloch));
        when(authorRepository.count()).thenReturn(12L);
        author = authorService.findAtRandom();

        //then
        assertNotNull(author);
        assertEquals(Bloch, author);
        verify(authorRepository, times(1)).count();
        verify(authorRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(authorRepository);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_fail_while_finding_author_at_random_when_there_is_no_author() {
        //given
        final Author author;

        //when
        when(authorRepository.count()).thenReturn(0L);
        author = authorService.findAtRandom();

        //then
        assertNull(author);
        verify(authorRepository, times(1)).count();
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_save_new_author() {
        //given
        final Author Walls = Author.builder().id(6L).firstName("Craig").lastName("Walls").build();
        final AuthorCreateDTO WallsDTO = AuthorCreateDTO.builder().firstName("Craig").lastName("Walls").build();
        final Author author;

        //when
        when(authorRepository.save(any(Author.class))).thenReturn(Walls);
        author = authorService.save(WallsDTO);

        //then
        assertNotNull(author);
        assertEquals("Craig", author.getFirstName());
        assertEquals("Walls", author.getLastName());
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorRepository, times(1)).findByFirstNameAndLastName("Craig", "Walls");
        verifyNoMoreInteractions(authorRepository);
    }

    @Test(expected = DuplicateResourceException.class)
    public void should_fail_while_saving_author_that_exists() {
        //given
        AuthorCreateDTO WallsDTO = AuthorCreateDTO.builder().firstName("Craig").lastName("Walls").build();
        final Author author;

        //when
        when(authorRepository.findByFirstNameAndLastName("Craig", "Walls")).thenThrow(DuplicateResourceException.class);
        author = authorService.save(WallsDTO);

        //then
        assertNull(author);
        verify(authorRepository, times(1)).findByFirstNameAndLastName("Craig", "Walls");
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_update_all_author_attributes() {
        //given
        final Author Bloch_to_check_updating_all_attributes = Author.builder().id(11L).firstName("Joshua").lastName("Bloch").build();
        final Author Horstmann_to_check_updating_firstName = Author.builder().id(12L).firstName("Cay S.").lastName("Horstmann").build();
        final Author Horstmann_to_check_updating_lastName = Author.builder().id(13L).firstName("Cay S.").lastName("Horstmann").build();

        final AuthorCreateDTO WallsDTO_to_check_updating_all_attributes = AuthorCreateDTO.builder().firstName("Craig").lastName("Walls").build();
        final AuthorCreateDTO WallsDTO_to_check_updating_firstName = AuthorCreateDTO.builder().firstName("Craig").lastName("").build();
        final AuthorCreateDTO WallsDTO_to_check_updating_lastName = AuthorCreateDTO.builder().firstName("").lastName("Walls").build();

        final Author author_to_check_updating_all_attributes, author_to_check_updating_firstName, author_to_check_updating_lastName;

        //when
        author_to_check_updating_all_attributes = authorService.updateAttributesOfAuthorFound(
                Bloch_to_check_updating_all_attributes, WallsDTO_to_check_updating_all_attributes
        );
        author_to_check_updating_firstName = authorService.updateAttributesOfAuthorFound(
                Horstmann_to_check_updating_firstName, WallsDTO_to_check_updating_firstName
        );
        author_to_check_updating_lastName = authorService.updateAttributesOfAuthorFound(
                Horstmann_to_check_updating_lastName, WallsDTO_to_check_updating_lastName
        );

        //then
        assertNotNull(author_to_check_updating_all_attributes);
        assertEquals(11L, (long) author_to_check_updating_all_attributes.getId());
        assertEquals("Craig", author_to_check_updating_all_attributes.getFirstName());
        assertEquals("Walls", author_to_check_updating_all_attributes.getLastName());

        assertNotNull(author_to_check_updating_firstName);
        assertEquals(12L, (long) author_to_check_updating_firstName.getId());
        assertEquals("Craig", author_to_check_updating_firstName.getFirstName());
        assertEquals("Horstmann", author_to_check_updating_firstName.getLastName());

        assertNotNull(author_to_check_updating_lastName);
        assertEquals(13L, (long) author_to_check_updating_lastName.getId());
        assertEquals("Cay S.", author_to_check_updating_lastName.getFirstName());
        assertEquals("Walls", author_to_check_updating_lastName.getLastName());

        verifyNoInteractions(authorRepository);
    }

    @Test
    public void should_update_author() {
        //given
        final Author Bloch = Author.builder().id(11L).firstName("Joshua").lastName("Bloch").build();

        final AuthorCreateDTO WallsDTO = AuthorCreateDTO.builder().firstName("Craig").lastName("Walls").build();

        final Author author;

        //when
        author = authorService.updateFoundAuthor(Bloch, WallsDTO);

        //then
        assertNotNull(author);
        assertEquals(11L, (long) author.getId());
        assertEquals("Craig", author.getFirstName());
        assertEquals("Walls", author.getLastName());
        verifyNoInteractions(authorRepository);
    }

    @Test(expected = WrongDataProvidedException.class)
    public void should_fail_while_updating_author_when_firstName_is_empty_String() {
        //given
        final Author Bloch = Author.builder().id(11L).firstName("Joshua").lastName("Bloch").build();

        final AuthorCreateDTO WallsDTO = AuthorCreateDTO.builder().firstName("").lastName("Walls").build();

        final Author author;

        //when
        author = authorService.updateFoundAuthor(Bloch, WallsDTO);

        //then
        assertNull(author);
        verifyNoInteractions(authorRepository);
    }

    @Test(expected = WrongDataProvidedException.class)
    public void should_fail_while_updating_author_when_lastName_is_empty_String() {
        //given
        final Author Bloch = Author.builder().id(11L).firstName("Joshua").lastName("Bloch").build();

        final AuthorCreateDTO WallsDTO = AuthorCreateDTO.builder().firstName("Craig").lastName("").build();

        final Author author;

        //when
        author = authorService.updateFoundAuthor(Bloch, WallsDTO);

        //then
        assertNull(author);
        verifyNoInteractions(authorRepository);
    }

    @Test
    public void should_delete_author() {
        //given
        final Author Walls = Author.builder().id(6L).firstName("Craig").lastName("Walls").build();

        //when
        when(authorRepository.findById(6L)).thenReturn(Optional.of(Walls));
        doNothing().when(authorRepository).delete(Walls);
        authorService.delete(6L);

        //then
        verify(authorRepository, times(2)).findById(6L);
        verify(authorRepository, times(1)).delete(any(Author.class));
        verifyNoMoreInteractions(authorRepository);
    }

    @Test
    public void should_do_nothing_while_trying_to_delete_nonexistent_author() {
        //when
        when(authorRepository.findById(6L)).thenReturn(Optional.empty());
        authorService.delete(6L);

        //then
        verify(authorRepository, times(1)).findById(6L);
        verifyNoMoreInteractions(authorRepository);
    }
}
