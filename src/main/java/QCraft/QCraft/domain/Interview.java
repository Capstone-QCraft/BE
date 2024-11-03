package QCraft.QCraft.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private List<String> feedback;
    private LocalDateTime createdAt;

    @DBRef
    private Member member;

    @DBRef
    @Indexed(unique = true)
    private ResumeFile resumeFile;
}
