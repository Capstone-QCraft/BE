package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.domain.ResumeFileText;
import QCraft.QCraft.repository.ResumeFileTextRepository;
import QCraft.QCraft.service.ResumeFileTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeFileTextServiceImpl implements ResumeFileTextService {

    @Override
    public ResumeFileText createResumeFileText(ResumeFile resumeFile, String extension) {
        try{
            ResumeFileText resumeFileText = new ResumeFileText();

            String content = runPythonScript(extension, resumeFile);

            if(content == null||content.isEmpty()||content.equals("pyerror")) {
                System.out.println("123123");
                return null;
            }

            //saveExtractedTextToFile(content, resumeFile);

            resumeFileText.setContent(content);
            resumeFileText.setResumeFile(resumeFile);

            return resumeFileText;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private String runPythonScript(String extension, ResumeFile resumeFile) {
        try{
            String scriptPath = "src/main/python/extract_text.py";
            String resumeFilePath = resumeFile.getPath();

            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath, extension, resumeFilePath);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder extractedText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("python 출력 :"+line);
                if(line.equals("pyerror")){
                    return null;
                }
                extractedText.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            System.out.println("python 스크립트 종료 코드: "+exitCode);

            if(exitCode != 0) {
                System.out.println("파이썬 오류");
                return "pyerror";
            }

            return extractedText.toString().trim();

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("888");
            return null;
        }
    }

    private void saveExtractedTextToFile(String content, ResumeFile resumeFile) {
        try{
            Path directoryPath = Paths.get("src/main/resources/extractedText");
            if(!Files.exists(directoryPath)) {
                Files.createDirectory(directoryPath);
            }

            String fileName = UUID.randomUUID()+resumeFile.getFilename().substring(resumeFile.getFilename().indexOf("_")+1) + ".txt";
            Path filePath = directoryPath.resolve(fileName);

            try(BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write(content);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
