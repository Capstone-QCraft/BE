package QCraft.QCraft.dto.response.file;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Getter
@NoArgsConstructor
public class GetFileResponseDTO extends ResponseDTO {
    private String fileName;
    private String filePath;
    private Date uploadTime;

    public GetFileResponseDTO(String fileName, String filePath, Date uploadTime) {
        super();
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadTime = uploadTime;
    }

    public static ResponseEntity<GetFileResponseDTO> success(String fileName, String filePath, Date uploadTime) {
        GetFileResponseDTO responseDTO = new GetFileResponseDTO(fileName, filePath, uploadTime);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> fileNotFound() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.FILE_NOT_FOUND, ResponseMessage.FILE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
