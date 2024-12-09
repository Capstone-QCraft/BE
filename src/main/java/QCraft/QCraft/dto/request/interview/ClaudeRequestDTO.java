package QCraft.QCraft.dto.request.interview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClaudeRequestDTO {
    private String model;
    @JsonProperty("max_tokens")
    private int maxTokens;
    private double temperature;
    private String system;
    private List<Message> messages;

    @Getter
    @Setter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }

}
