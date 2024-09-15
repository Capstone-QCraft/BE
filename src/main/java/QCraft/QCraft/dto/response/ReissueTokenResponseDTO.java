package QCraft.QCraft.dto.response;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ReissueTokenResponseDTO extends ResponseDTO {
    private String accessToken;
    private String refreshToken;

    private ReissueTokenResponseDTO(String accessToken, String refreshToken) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ResponseEntity<ReissueTokenResponseDTO> success(String accessToken, String refreshToken) {
        ReissueTokenResponseDTO responseBody = new ReissueTokenResponseDTO(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> tokenExpiration() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.EXPIRED_TOKEN, ResponseMessage.EXPIRED_TOKEN);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
