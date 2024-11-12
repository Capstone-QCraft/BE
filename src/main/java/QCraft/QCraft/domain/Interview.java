package QCraft.QCraft.domain;

import QCraft.QCraft.dto.response.interview.FeedbackResult;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "interview")
public class Interview {
    @Id
    private String id;
    private List<String> questions;
    private List<String> answers;
    private List<List<String>> positivePoint;
    private List<List<String>> improvement;
    private String overallSuggestion;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private String memberId;

    private ResumeFile resumeFile;//중첩 사용
}
