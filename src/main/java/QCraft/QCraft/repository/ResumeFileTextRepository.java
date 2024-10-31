package QCraft.QCraft.repository;

import QCraft.QCraft.domain.ResumeFileText;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeFileTextRepository extends MongoRepository<ResumeFileText, String> {
}
