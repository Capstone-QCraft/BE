package QCraft.QCraft.repository;

import QCraft.QCraft.domain.Certification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CertificationRepository extends MongoRepository<Certification, String> {
    Optional<Certification> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
