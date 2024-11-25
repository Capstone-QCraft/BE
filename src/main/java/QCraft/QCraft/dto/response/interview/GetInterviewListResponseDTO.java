package QCraft.QCraft.dto.response.interview;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class GetInterviewListResponseDTO extends ResponseDTO {
    List<InterviewSummaryDTO> data;
    long totalInterviews;

    private GetInterviewListResponseDTO(List<InterviewSummaryDTO> data, Long totalInterviews) {
        super();
        this.data = data;
        this.totalInterviews = totalInterviews;
    }

    public static ResponseEntity<GetInterviewListResponseDTO> success(List<InterviewSummaryDTO> data, Long totalInterviews) {
        GetInterviewListResponseDTO responseDTO = new GetInterviewListResponseDTO(data, totalInterviews);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> interviewNotFound() {
        ResponseDTO responseDTO = new ResponseDTO(ResponseCode.INTERVIEW_NOT_FOUND, ResponseMessage.INTERVIEW_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> pageOutOfBounds() {
        ResponseDTO responseDTO = new ResponseDTO(ResponseCode.PAGE_OUT_OF_BOUNDS, ResponseMessage.PAGE_OUT_OF_BOUNDS);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
