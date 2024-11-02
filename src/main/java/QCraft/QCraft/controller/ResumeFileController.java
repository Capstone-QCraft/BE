package QCraft.QCraft.controller;

import QCraft.QCraft.dto.request.file.UploadFileRequestDTO;
import QCraft.QCraft.dto.response.file.DeleteFileResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileListResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileResponseDTO;
import QCraft.QCraft.dto.response.file.UploadFileResponseDTO;
import QCraft.QCraft.service.ResumeFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class ResumeFileController {

    private final ResumeFileService resumeFileService;

    @PostMapping("/upload")
    public ResponseEntity<? super UploadFileResponseDTO> UploadFile(@RequestParam("file") MultipartFile file) {
        UploadFileRequestDTO uploadFileRequestDTO = new UploadFileRequestDTO();
        uploadFileRequestDTO.setFile(file);
        return resumeFileService.uploadFile(uploadFileRequestDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<? super GetFileListResponseDTO> getFileList() {
        return resumeFileService.getFileList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<? super GetFileResponseDTO> getFile(@PathVariable String id) {
        return resumeFileService.getFile(id);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<? super DeleteFileResponseDTO> deleteFile(@PathVariable String id) {
        return resumeFileService.deleteFile(id);
    }


}
