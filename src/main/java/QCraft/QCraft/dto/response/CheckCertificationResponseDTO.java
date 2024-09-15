package QCraft.QCraft.dto.response;

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
        ResponseDTO responseBody = new ResponseDTO();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
