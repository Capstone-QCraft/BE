package QCraft.QCraft.repository;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.ResumeFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeFileRepository extends MongoRepository<ResumeFile, String> {
    Optional<List<ResumeFile>> findByMemberId(String memberId);
    void deleteByMemberId(String MemberId);
}
