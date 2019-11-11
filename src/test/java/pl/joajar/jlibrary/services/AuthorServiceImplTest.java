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
import pl.joajar.jlibrary.repository.AuthorRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        verify(authorRepository, times(1)).findAll();
        verifyNoMoreInteractions(authorRepository);
    }
}
