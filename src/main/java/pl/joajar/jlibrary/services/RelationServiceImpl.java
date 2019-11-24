package pl.joajar.jlibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.joajar.jlibrary.domain.Relation;
import pl.joajar.jlibrary.repository.RelationRepository;

import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {
    private RelationRepository relationRepository;

    @Autowired
    public RelationServiceImpl(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    @Override
    public List<Relation> findRelationByBookId(Long bookId) {
        return relationRepository.findRelationByBook_Id(bookId);
    }
}
