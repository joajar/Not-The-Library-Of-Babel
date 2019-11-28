package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.joajar.jlibrary.domain.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
