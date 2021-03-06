package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.joajar.jlibrary.domain.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findByLastNameIgnoringCaseContainingOrderById(String lastNameFragment);

    List<Author> findByFirstNameIgnoringCaseContainingOrderById(String firstNameFragment);
}
