package QCraft.QCraft.dto.response.member;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class LogOutResponseDTO extends ResponseDTO {
    private LogOutResponseDTO() {
        super();
    }

    public static ResponseEntity<LogOutResponseDTO> success(){
        LogOutResponseDTO responseBody = new LogOutResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> alreadyLogOut(){
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.EXPIRED_TOKEN, ResponseMessage.EXPIRED_TOKEN);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
