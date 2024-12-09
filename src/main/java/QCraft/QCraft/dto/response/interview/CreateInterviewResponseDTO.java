package QCraft.QCraft.dto.response.interview;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateInterviewResponseDTO extends ResponseDTO {
    private String interviewId;
    private List<String> questions;
    private LocalDateTime createdAt;

    private CreateInterviewResponseDTO(String interviewId, List<String> questions, LocalDateTime createdAt) {
        super();
        this.interviewId = interviewId;
        this.questions = questions;
        this.createdAt = createdAt;
    }

    public static ResponseEntity<CreateInterviewResponseDTO> success(Interview interview) {
        CreateInterviewResponseDTO responseDTO = new CreateInterviewResponseDTO(interview.getId(), interview.getQuestions(), interview.getCreatedAt());
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> failCreate() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.QUESTION_GENERATION_FAILED, ResponseMessage.QUESTION_GENERATION_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

}
