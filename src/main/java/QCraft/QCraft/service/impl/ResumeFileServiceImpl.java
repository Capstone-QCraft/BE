package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.domain.ResumeFileText;
import QCraft.QCraft.dto.request.file.UploadFileRequestDTO;
import QCraft.QCraft.dto.response.file.DeleteFileResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileListResponseDTO;
import QCraft.QCraft.dto.response.file.GetFileResponseDTO;
import QCraft.QCraft.dto.response.file.UploadFileResponseDTO;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import QCraft.QCraft.repository.MemberRepository;
import QCraft.QCraft.repository.ResumeFileRepository;
import QCraft.QCraft.repository.ResumeFileTextRepository;
import QCraft.QCraft.service.GetAuthenticationService;
import QCraft.QCraft.service.ResumeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeFileServiceImpl implements ResumeFileService {
    private final ResumeFileRepository resumeFileRepository;
    private final MemberRepository memberRepository;
    private final ResumeFileTextRepository resumeFileTextRepository;

    private final GetAuthenticationService getAuthenticationService;
    private final ResumeFileTextServiceImpl resumeFileTextService;

    @Value("${file.upload.directory}")
    private String uploadDirectory;


    @Override
    public ResponseEntity<? super UploadFileResponseDTO> uploadFile(UploadFileRequestDTO uploadFileRequestDTO) {
        try {
            MultipartFile file = uploadFileRequestDTO.getFile();

            if (file.isEmpty()) {
                return UploadFileResponseDTO.fileNotFound();
            }
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = saveFile(file, filename);
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            LocalDateTime currentDate = LocalDateTime.now();


            ResumeFile resumeFile = createResumeFile(filename, filePath, extension, currentDate);
            ResumeFileText resumeFileText = resumeFileTextService.createResumeFileText(resumeFile, extension);

            if (resumeFileText == null) {
                System.out.println("1");
                return UploadFileResponseDTO.fileError();
            }

            if (resumeFileText.getContent().equals("python_error")) {
                return UploadFileResponseDTO.fileError();
            }

            resumeFileRepository.save(resumeFile);
            resumeFileTextRepository.save(resumeFileText);

            return UploadFileResponseDTO.success(resumeFile);

        } catch (IOException e) {
            e.printStackTrace();
            return UploadFileResponseDTO.fileError();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetFileResponseDTO> getFile(String fileId) {
        try {
            Optional<ResumeFile> resumeFileOptional = resumeFileRepository.findById(fileId);
            if (resumeFileOptional.isEmpty()) {
                return GetFileResponseDTO.fileNotFound();
            }

            ResumeFile resumeFile = resumeFileOptional.get();

            String filename = resumeFile.getFilename();
            String filePath = resumeFile.getPath();
            LocalDateTime uploadedDate = resumeFile.getUploadDate();

            return GetFileResponseDTO.success(filename, filePath, uploadedDate);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetFileListResponseDTO> getFileList() {
        try {
            Member member = getAuthenticationService.getAuthentication().get();
            Optional<List<ResumeFile>> resumeFileListOptional = resumeFileRepository.findByMember(member);

            if (resumeFileListOptional.isEmpty()) {
                return GetFileListResponseDTO.fileNotFound();
            }
            List<ResumeFile> resumeFileList = resumeFileListOptional.get();

            return GetFileListResponseDTO.success(resumeFileList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super DeleteFileResponseDTO> deleteFile(String fileId) {
        try {
            Optional<ResumeFile> resumeFileOptional = resumeFileRepository.findById(fileId);
            if (resumeFileOptional.isEmpty()) {
                return DeleteFileResponseDTO.fileNotFound();
            }

            ResumeFile resumeFile = resumeFileOptional.get();
            resumeFileTextRepository.deleteByResumeFile(resumeFile);
            resumeFileRepository.delete(resumeFile);

            return DeleteFileResponseDTO.success();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }

    }


    private String saveFile(MultipartFile file, String filename) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        return filePath.toString();
    }



    private ResumeFile createResumeFile(String filename, String filePath, String extension, LocalDateTime currentDate) throws Exception {


        String memberId = getAuthenticationService.getAuthentication().get().getId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return new ResumeFile(filename, filePath, extension, member);
    }


}
