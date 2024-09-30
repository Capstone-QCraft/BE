package QCraft.QCraft.dto.response;


import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UpdateMemberInfoResponseDTO extends ResponseDTO {
    private UpdateMemberInfoResponseDTO() {
        super();
    }

    public static ResponseEntity<UpdateMemberInfoResponseDTO> success(){
        UpdateMemberInfoResponseDTO responseDTO = new UpdateMemberInfoResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> passwordMismatch(){
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.PASSWORD_MISMATCH, ResponseMessage.PASSWORD_MISMATCH);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
