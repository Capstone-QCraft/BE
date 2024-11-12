package QCraft.QCraft.repository;

import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.ResumeFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends MongoRepository<Interview, String> {
    @Query(value = "{ 'memberId': ?0 }", fields = "{ 'id': 1, 'createdAt': 1, 'resumeFileName': 1 }")
    Page<Interview> findByMemberId(String memberId, Pageable pageable);
    Optional<Interview> findByResumeFile_Id(String resumeFileId);
    void deleteByMemberId(String memberId);
}
