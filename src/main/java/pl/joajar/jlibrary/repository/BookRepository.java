package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.joajar.jlibrary.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
