package QCraft.QCraft.dto.response.member;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class RefreshTokenResponseDTO extends ResponseDTO {
    private String accessToken;

    public RefreshTokenResponseDTO(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public static ResponseEntity<RefreshTokenResponseDTO> success(String accessToken) {
        RefreshTokenResponseDTO refreshTokenResponseDTO = new RefreshTokenResponseDTO(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenResponseDTO);
    }

    public static ResponseEntity<ResponseDTO> expiredRefreshToken() {
        ResponseDTO responseDTO = new ResponseDTO(ResponseCode.EXPIRED_TOKEN, ResponseMessage.EXPIRED_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
    }
}
