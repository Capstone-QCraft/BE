package QCraft.QCraft.repository;

import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.domain.ResumeFileText;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeFileTextRepository extends MongoRepository<ResumeFileText, String> {
    Optional<ResumeFileText> findByResumeFile_Id(String resumeFileId);

    void deleteByResumeFile_Id(String resumeFileId);
}
