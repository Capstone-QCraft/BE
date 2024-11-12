package QCraft.QCraft.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "file")
@NoArgsConstructor
public class ResumeFile {

    @Id
    private String id;

    private String filename; // 파일 이름

    private String path; //저장 경로

    private String extension; //확장자

    private LocalDateTime uploadDate; //업로드 시간

    private String memberId;

    public ResumeFile(String filename, String path, String extension, String memberId) {
        this.filename = filename;
        this.path = path;
        this.extension = extension;
        this.uploadDate = LocalDateTime.now();
        this.memberId = memberId;
    }
    public ResumeFile(String filename, String path, String extension,LocalDateTime uploadDate, String memberId) {
        this.filename = filename;
        this.path = path;
        this.extension = extension;
        this.uploadDate = uploadDate;
        this.memberId = memberId;
    }
}
