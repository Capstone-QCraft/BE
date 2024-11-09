package QCraft.QCraft.repository;

import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.ResumeFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends MongoRepository<Interview, String> {
    Page<Interview> findByMember(Member member, Pageable pageable);
    Optional<Interview> findByResumeFile(ResumeFile resumeFile);
    void deleteByMember(Member member);
}
