package QCraft.QCraft.dto.response.member;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCheckResponseDTO extends ResponseDTO {
    private EmailCheckResponseDTO() {
        super();
    }

    public static ResponseEntity<EmailCheckResponseDTO> success() {
        EmailCheckResponseDTO responseBody = new EmailCheckResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> duplicated() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);

    }
}
