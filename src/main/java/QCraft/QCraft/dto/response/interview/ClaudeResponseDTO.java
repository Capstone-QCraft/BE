package QCraft.QCraft.dto.response.interview;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClaudeResponseDTO {
    private String id;
    private String type;
    private String role;
    private List<Content> content;
    private String model;
    private String stopReason;
    private String stopSequence;
    private Usage usage;

    @Getter
    @Setter
    public static class Content {
        private String text;
    }

    @Getter
    @Setter
    public static class Usage {
        // 사용된 입력 및 출력 토큰 수 정보를 포함한 객체
        private int inputTokens;
        private int outputTokens;
    }
}
