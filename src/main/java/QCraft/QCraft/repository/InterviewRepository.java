package QCraft.QCraft.repository;

import QCraft.QCraft.MongoDBProjection.InterviewForListProjection;
import QCraft.QCraft.domain.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewRepository extends MongoRepository<Interview, String> {
    Page<InterviewForListProjection> findByMemberId(String memberId, Pageable pageable);
    Optional<Interview> findByResumeFile_Id(String resumeFileId);
    void deleteByMemberId(String memberId);
}
