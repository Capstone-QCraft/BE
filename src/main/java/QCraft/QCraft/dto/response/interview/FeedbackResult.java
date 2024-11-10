package QCraft.QCraft.dto.response.interview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResult {
    private List<List<String>> positivePoint;
    private List<List<String>> improvement;
    private String overallSuggestion;
}
