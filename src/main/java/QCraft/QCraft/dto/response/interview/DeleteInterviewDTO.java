package QCraft.QCraft.dto.response.interview;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class DeleteInterviewDTO extends ResponseDTO {
    private DeleteInterviewDTO() {
        super();
    }

    public static ResponseEntity<DeleteInterviewDTO> success() {
        DeleteInterviewDTO responseDTO = new DeleteInterviewDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> InterviewNotFound() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.INTERVIEW_NOT_FOUND, ResponseMessage.INTERVIEW_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
