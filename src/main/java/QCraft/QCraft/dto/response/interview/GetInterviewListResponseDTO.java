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
public class GetInterviewListResponseDTO extends ResponseDTO {
    private List<Interview> interviews;

    private GetInterviewListResponseDTO(List<Interview> interviews) {
        super();
        this.interviews = interviews;
    }

    public static ResponseEntity<GetInterviewListResponseDTO> success(List<Interview> interviews) {
        GetInterviewListResponseDTO responseDTO = new GetInterviewListResponseDTO(interviews);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> interviewNotFound() {
        ResponseDTO responseDTO = new ResponseDTO(ResponseCode.INTERVIEW_NOT_FOUND, ResponseMessage.INTERVIEW_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
