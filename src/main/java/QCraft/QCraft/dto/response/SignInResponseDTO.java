package QCraft.QCraft.dto.response;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDTO extends ResponseDTO {

    private String accessToken;
    private String freshToken;
    private int expiresIn;



    private SignInResponseDTO(String accessToken, String freshToken, int expiresIn) {
        super();
        this.accessToken = accessToken;
        this.freshToken = freshToken;
        this.expiresIn = expiresIn;
    }

    public static ResponseEntity<SignInResponseDTO> success(String accessToken, String freshToken, int expiresIn) {
        SignInResponseDTO responseBody = new SignInResponseDTO(accessToken,freshToken, expiresIn);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> signInFailed() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.SIGN_IN_FAILED, ResponseMessage.SIGN_IN_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }


}
