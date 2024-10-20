package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.Member;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.dto.request.file.UploadFileRequestDTO;
import QCraft.QCraft.dto.response.file.UploadFileResponseDTO;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import QCraft.QCraft.repository.MemberRepository;
import QCraft.QCraft.repository.ResumeFileRepository;
import QCraft.QCraft.service.GetAuthenticationService;
import QCraft.QCraft.service.ResumeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeFileServiceImpl implements ResumeFileService {
    private final ResumeFileRepository resumeFileRepository;
    private final MemberRepository memberRepository;

    private final GetAuthenticationService getAuthenticationService;

    private final Environment environment;

    @Value("${file.upload.directory.dev}")
    private String devUploadDirectory;

    // 배포 환경 경로
    @Value("${file.upload.directory.prod}")
    private String prodUploadDirectory;


    @Override
    public ResponseEntity<? super UploadFileResponseDTO> uploadFile(UploadFileRequestDTO uploadFileRequestDTO) {
        try{
            MultipartFile file = uploadFileRequestDTO.getFile();

            if(file.isEmpty()){
                return UploadFileResponseDTO.fileNotFound();
            }
            String filename = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
            String filePath = saveFile(file, filename);

            ResumeFile resumeFile = createResumeFile(filename, filePath);

            resumeFileRepository.save(resumeFile);

            return UploadFileResponseDTO.success(filename, filePath);

        }catch (IOException e){
            e.printStackTrace();
            return UploadFileResponseDTO.fileError();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }


    }



    private String saveFile(MultipartFile file, String filename) throws IOException {
        Path uploadPath = getUploadPath();
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


        return filePath.toString();
    }

    private Path getUploadPath() {
        if (environment.acceptsProfiles("prod")) {
            // 배포 환경
            return Paths.get(prodUploadDirectory);
        } else {
            // 개발 환경
            return Paths.get(System.getProperty("user.dir"), devUploadDirectory);
        }
    }

    private ResumeFile createResumeFile(String filename, String filePath) throws Exception {
        ResumeFile resumeFile = new ResumeFile();
        resumeFile.setFilename(filename);
        resumeFile.setPath(filePath);


        String memberId = getAuthenticationService.getAuthentication().get().getId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("Member not found"));
        resumeFile.setMember(member);

        return resumeFile;

    }
}
