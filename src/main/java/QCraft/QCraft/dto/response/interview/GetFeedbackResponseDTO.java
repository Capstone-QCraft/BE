package QCraft.QCraft.dto.response.interview;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class GetFeedbackResponseDTO extends ResponseDTO {
    private String interviewId;
    private List<String> feedback;

    private GetFeedbackResponseDTO(String interviewId, List<String> feedback) {
       super();
       this.interviewId = interviewId;
       this.feedback = feedback;
    }

    public static ResponseEntity<GetFeedbackResponseDTO> success(Interview interview) {
        GetFeedbackResponseDTO responseDTO = new GetFeedbackResponseDTO(interview.getId(), interview.getFeedback());
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> failedGetFeedback() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.FEEDBACK_GENERATION_FAILED, ResponseMessage.FEEDBACK_GENERATION_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}