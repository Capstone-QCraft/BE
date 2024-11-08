package QCraft.QCraft.controller;

import QCraft.QCraft.dto.request.file.UploadFileRequestDTO;
import QCraft.QCraft.dto.response.file.DeleteFileResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileListResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileResponseDTO;
import QCraft.QCraft.dto.response.file.UploadFileResponseDTO;
import QCraft.QCraft.service.ResumeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class ResumeFileController {

    private final ResumeFileService resumeFileService;

    //파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<? super UploadFileResponseDTO> UploadFile(@RequestParam("file") MultipartFile file) {
        UploadFileRequestDTO uploadFileRequestDTO = new UploadFileRequestDTO();
        uploadFileRequestDTO.setFile(file);
        return resumeFileService.uploadFile(uploadFileRequestDTO);
    }

    //파일 리스트 불러오기
    @GetMapping("/list")
    public ResponseEntity<? super GetFileListResponseDTO> getFileList() {
        return resumeFileService.getFileList();
    }

    //파일 상세 정보
    @GetMapping("/{ResumeFileid}")
    public ResponseEntity<? super GetFileResponseDTO> getFile(@PathVariable String ResumeFileid) {
        return resumeFileService.getFile(ResumeFileid);
    }

    //파일 삭제
    @DeleteMapping("delete/{ResumeFileid}")
    public ResponseEntity<? super DeleteFileResponseDTO> deleteFile(@PathVariable String ResumeFileidid) {
        return resumeFileService.deleteFile(ResumeFileidid);
    }


}
