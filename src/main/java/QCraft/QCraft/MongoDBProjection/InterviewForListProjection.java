package QCraft.QCraft.MongoDBProjection;

import java.time.LocalDateTime;

public interface InterviewForListProjection {
    String getId();
    LocalDateTime getCreatedAt();
    ResumeFileProjection getResumeFile();

    interface ResumeFileProjection {
        String getFilename();
    }
}
