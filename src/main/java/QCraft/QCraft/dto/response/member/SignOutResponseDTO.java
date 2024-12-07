package QCraft.QCraft.dto.response.member;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class SignOutResponseDTO extends ResponseDTO {
    private SignOutResponseDTO() {
        super();
    }

    public static ResponseEntity<SignOutResponseDTO> success() {
        SignOutResponseDTO responseBody = new SignOutResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> alreadyLogOut() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.EXPIRED_TOKEN, ResponseMessage.EXPIRED_TOKEN);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
