package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.joajar.jlibrary.domain.Book;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPublicationDateBetweenOrderById(LocalDate lowerBound, LocalDate upperBound);
}
