package QCraft.QCraft.dto.response.interview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InterviewSummaryDTO {
    private String interviewId;
    private LocalDateTime createdAt;
    private String fileName;
}
