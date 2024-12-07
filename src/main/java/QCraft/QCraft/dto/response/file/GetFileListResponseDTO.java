package QCraft.QCraft.dto.response.file;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetFileListResponseDTO extends ResponseDTO {
    private List<ResumeFile> files;


    private GetFileListResponseDTO(List<ResumeFile> files) {
        super();
        this.files = files;
    }

    public static ResponseEntity<GetFileListResponseDTO> success(List<ResumeFile> files) {
        GetFileListResponseDTO responseDTO = new GetFileListResponseDTO(files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> fileNotFound() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.FILE_NOT_FOUND, ResponseMessage.FILE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}
