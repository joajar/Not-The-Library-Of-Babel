package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.joajar.jlibrary.domain.Relation;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findRelationByBook_Id(Long bookId);
}
