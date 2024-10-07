package QCraft.QCraft.dto.response;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CheckCertificationResponseDTO extends ResponseDTO {

    private CheckCertificationResponseDTO() {
        super();
    }

    public static ResponseEntity<CheckCertificationResponseDTO> success() {
        CheckCertificationResponseDTO responseDTO = new CheckCertificationResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> certificationFailed() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.CERTIFICATE_EXPIRED, ResponseMessage.CERTIFICATE_EXPIRED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
