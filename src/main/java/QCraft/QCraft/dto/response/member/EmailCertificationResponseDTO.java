package QCraft.QCraft.dto.response.member;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCertificationResponseDTO extends ResponseDTO {

    private EmailCertificationResponseDTO() {
        super();
    }

    public static ResponseEntity<EmailCertificationResponseDTO> success() {
        EmailCertificationResponseDTO responseBody = new EmailCertificationResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> mailSendFailed() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.MAIL_FAILED, ResponseMessage.MAIL_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
