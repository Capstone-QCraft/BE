package QCraft.QCraft.dto.response.interview;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetInterviewResponseDTO extends ResponseDTO {
    private Interview interview;

    private GetInterviewResponseDTO(Interview interview) {
        super();
        this.interview = interview;

    }

    public static ResponseEntity<GetInterviewResponseDTO> success(Interview interview) {
        GetInterviewResponseDTO responseDTO = new GetInterviewResponseDTO(interview);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> interviewNotFound() {
        ResponseDTO responseDTO = new ResponseDTO(ResponseCode.INTERVIEW_NOT_FOUND, ResponseMessage.INTERVIEW_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
