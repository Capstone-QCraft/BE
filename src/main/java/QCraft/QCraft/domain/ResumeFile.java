package QCraft.QCraft.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "file")
@NoArgsConstructor
public class ResumeFile {

    @Id
    private String id;

    private String filename; // 파일 이름

    private String path; //저장 경로

    private String extension; //확장자

    @DBRef
    private Member member;
}
