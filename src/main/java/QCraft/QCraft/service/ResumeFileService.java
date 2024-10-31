package QCraft.QCraft.service;

import QCraft.QCraft.dto.request.file.UploadFileRequestDTO;
import QCraft.QCraft.dto.response.file.UploadFileResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeFileService {

    ResponseEntity<? super UploadFileResponseDTO> uploadFile(UploadFileRequestDTO uploadFileRequestDTO);
}
