package pl.joajar.jlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.joajar.jlibrary.domain.Relation;

import java.util.List;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findRelationByBook_Id(Long bookId);
}
