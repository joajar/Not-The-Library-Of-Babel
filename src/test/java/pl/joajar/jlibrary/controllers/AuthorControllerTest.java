package pl.joajar.jlibrary.controllers;

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
import pl.joajar.jlibrary.exceptions.ResourceNotFoundException;
import pl.joajar.jlibrary.services.AuthorServiceImpl;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.firstName", Matchers.is(Bloch.getFirstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(Bloch.getLastName())));

        verify(authorService, times(1)).findById(1L);
        verifyNoMoreInteractions(authorService);
    }

    @Test
    public void should_get_nonexistent_author_by_id_fail() throws Exception {
        //when
        when(authorService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(get("/v1/library/authors/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).findById(2L);
        verifyNoMoreInteractions(authorService);
    }
}
