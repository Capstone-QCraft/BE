package QCraft.QCraft.repository;

import QCraft.QCraft.domain.ResumeFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeFileRepository extends MongoRepository<ResumeFile, String> {

}
