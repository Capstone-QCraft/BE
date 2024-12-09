package QCraft.QCraft.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "resumeFileText")
@NoArgsConstructor
public class ResumeFileText {
    @Id
    private String id;

    private String content;

    private ResumeFile resumeFile;

}
