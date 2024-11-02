package QCraft.QCraft.dto.response.file;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DeleteFileResponseDTO extends ResponseDTO {
    private DeleteFileResponseDTO() {
        super();
    }

    public static ResponseEntity<DeleteFileResponseDTO> success() {
        DeleteFileResponseDTO response = new DeleteFileResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static ResponseEntity<ResponseDTO> fileNotFound() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.FILE_NOT_FOUND, ResponseMessage.FILE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
