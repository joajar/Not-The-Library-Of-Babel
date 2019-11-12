package pl.joajar.jlibrary.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.joajar.jlibrary.domain.Author;
import pl.joajar.jlibrary.services.AuthorService;

import java.util.Arrays;

@Component
public class AnnotationDrivenContextStartedListener {
    private AuthorService authorService;

    @Autowired
    public AnnotationDrivenContextStartedListener(AuthorService authorService) {
        this.authorService = authorService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillInDatabase(){
        Author Horstmann = Author.builder().id(1L).firstName("Cay S.").lastName("Horstmann").build();
        Author Bloch = Author.builder().id(2L).firstName("Joshua").lastName("Bloch").build();
        Author Bauer = Author.builder().id(3L).firstName("Christian").lastName("Bauer").build();
        Author King = Author.builder().id(4L).firstName("Gavin").lastName("King").build();
        Author Gregory = Author.builder().id(5L).firstName("Gary").lastName("Gregory").build();
        Author Walls = Author.builder().id(6L).firstName("Craig").lastName("Walls").build();

        authorService.saveAll(Arrays.asList(Horstmann, Bloch, Bauer, King, Gregory, Walls));
    }
}
