package QCraft.QCraft.service;

import QCraft.QCraft.dto.request.file.UploadFileRequestDTO;
import QCraft.QCraft.dto.response.file.DeleteFileResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileListResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileResponseDTO;
import QCraft.QCraft.dto.response.file.UploadFileResponseDTO;
import org.springframework.http.ResponseEntity;

public interface ResumeFileService {

    ResponseEntity<? super UploadFileResponseDTO> uploadFile(UploadFileRequestDTO uploadFileRequestDTO);
    ResponseEntity<? super GetFileResponseDTO> getFile(String fileId);
    ResponseEntity<? super GetFileListResponseDTO> getFileList();
    ResponseEntity<? super DeleteFileResponseDTO> deleteFile(String fileId);
}
