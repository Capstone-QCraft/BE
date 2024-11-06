package QCraft.QCraft.dto.response.file;

import QCraft.QCraft.commons.ResponseCode;
import QCraft.QCraft.commons.ResponseMessage;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.dto.response.member.CheckCertificationResponseDTO;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class UploadFileResponseDTO extends ResponseDTO {
    private String fileId;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadTime;

    private UploadFileResponseDTO(String fileId, String fileName, String filePath, LocalDateTime uploadTime) {
        super();
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadTime = uploadTime;
    }

    public static ResponseEntity<UploadFileResponseDTO> success(ResumeFile resumeFile) {
        UploadFileResponseDTO responseDTO = new UploadFileResponseDTO(resumeFile.getId(), resumeFile.getFilename(), resumeFile.getPath(), resumeFile.getUploadDate());
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    public static ResponseEntity<ResponseDTO> fileError(){
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.FILE_ERROR, ResponseMessage.FILE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> fileNotFound(){
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.FILE_NOT_FOUND, ResponseMessage.FILE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
