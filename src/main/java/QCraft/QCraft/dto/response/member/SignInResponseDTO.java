package QCraft.QCraft.dto.response.member;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDTO extends ResponseDTO {

    private String accessToken;

    private SignInResponseDTO(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public static ResponseEntity<SignInResponseDTO> success(String accessToken) {
        SignInResponseDTO responseBody = new SignInResponseDTO(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> signInFailed() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.SIGN_IN_FAILED, ResponseMessage.SIGN_IN_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> passwordMismatch() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.PASSWORD_MISMATCH, ResponseMessage.PASSWORD_MISMATCH);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
