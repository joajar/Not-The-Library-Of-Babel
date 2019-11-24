package pl.joajar.jlibrary.services;

import pl.joajar.jlibrary.domain.Relation;

import java.util.List;

public interface RelationService {
    List<Relation> findRelationByBookId(Long bookId);
}
