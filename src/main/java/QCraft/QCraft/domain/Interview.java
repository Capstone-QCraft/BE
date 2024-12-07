package QCraft.QCraft.domain;

import QCraft.QCraft.dto.response.interview.FeedbackResult;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "interview")
@CompoundIndexes({
        @CompoundIndex(name = "member_created_idx", def = "{'memberId': 1, 'createdAt': -1}")
})
public class Interview {
    @Id
    private String id;
    private List<String> questions;
    private List<String> answers;
    private List<List<String>> positivePoint;
    private List<List<String>> improvement;
    private String overallSuggestion;

    private LocalDateTime createdAt;

    private String memberId;

    private ResumeFile resumeFile;//중첩 사용

}
