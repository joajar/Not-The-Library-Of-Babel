package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.joajar.jlibrary.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
