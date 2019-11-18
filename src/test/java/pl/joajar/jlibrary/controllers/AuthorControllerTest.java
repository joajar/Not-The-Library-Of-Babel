package pl.joajar.jlibrary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.dto.AuthorDTO;
import pl.joajar.jlibrary.exceptions.DuplicateResourceException;
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.services.AuthorServiceImpl;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthorControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthorServiceImpl authorService;

    @InjectMocks
    private AuthorController authorController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    public void should_get_all_authors() throws Exception {
        //given
        final Author Horstmann = Author.builder().id(1L).firstName("Cay S.").lastName("Horstmann").build();
        final Author Bloch = Author.builder().id(2L).firstName("Joshua").lastName("Bloch").build();

        //when
        when(authorService.findAll()).thenReturn(Arrays.asList(Horstmann, Bloch));

        //then
        mockMvc.perform(get("/v1/library/authors"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Cay S.")))
                .andExpect(jsonPath("$[0].lastName", is("Horstmann")))
                .andExpect(jsonPath("$[1].firstName", is("Joshua")))
                .andExpect(jsonPath("$[1].lastName", is("Bloch")));

        verify(authorService, times(1)).findAll();
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_get_author_by_id() throws Exception {
        //given
        final Author Bloch = Author.builder().id(1L).firstName("Joshua").lastName("Bloch").build();

        //when
        when(authorService.findById(1L)).thenReturn(Bloch);

        //then
        mockMvc.perform(get("/v1/library/authors/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is(Bloch.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(Bloch.getLastName())));

        verify(authorService, times(1)).findById(1L);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_fail_while_getting_nonexistent_author() throws Exception {
        //when
        when(authorService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/authors/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_get_author_at_random() throws Exception {
        //given
        final Author Bloch = Author.builder().id(1L).firstName("Joshua").lastName("Bloch").build();

        //when
        when(authorService.findAtRandom()).thenReturn(Bloch);

        //then
        mockMvc.perform(get("/v1/library/authors/random").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is(Bloch.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(Bloch.getLastName())));

        verify(authorService, times(1)).findAtRandom();
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_fail_while_getting_at_random_when_there_is_no_author_at_the_database() throws Exception {
        //when
        when(authorService.findAtRandom()).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/authors/random").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).findAtRandom();
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_post_author() throws Exception {
        //given
        final Author Bloch = Author.builder().id(1L).firstName("Joshua").lastName("Bloch").build();
        final AuthorDTO BlochDTO = AuthorDTO.builder().firstName("Joshua").lastName("Bloch").build();

        //when
        when(authorService.save(any(AuthorDTO.class))).thenReturn(Bloch);

        //then
        mockMvc.perform(
                post("/v1/library/authors")
                        .content(asJsonString(BlochDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is(BlochDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(BlochDTO.getLastName())))
        ;// ; put in the last line in order to allow work on particular lines of the test separately, similarly as in SQL

        verify(authorService, times(1)).save(any(AuthorDTO.class));
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_fail_while_posting_author_that_exists_in_the_db() throws Exception {
        //given
        final AuthorDTO BlochDTO = AuthorDTO.builder().firstName("Joshua").lastName("Bloch").build();

        //when
        when(authorService.save(any(AuthorDTO.class))).thenThrow(DuplicateResourceException.class);

        //then
        mockMvc.perform(
                post("/v1/library/authors")
                        .content(asJsonString(BlochDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
        ;

        verify(authorService, times(1)).save(any(AuthorDTO.class));
        verifyNoMoreInteractions(authorService);
    }


    /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
