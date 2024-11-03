package QCraft.QCraft.dto.request.interview;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetFeedbackRequestDTO {
    private String interviewId;
    private List<String> answers;
}
