package QCraft.QCraft.service.impl;

import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.domain.ResumeFileText;
import QCraft.QCraft.dto.request.interview.GetFeedbackRequestDTO;
import QCraft.QCraft.dto.response.file.GetFileResponseDTO;
import QCraft.QCraft.dto.response.interview.*;
import QCraft.QCraft.dto.response.member.ResponseDTO;
import QCraft.QCraft.repository.InterviewRepository;
import QCraft.QCraft.repository.ResumeFileRepository;
import QCraft.QCraft.repository.ResumeFileTextRepository;
import QCraft.QCraft.service.ClaudeApiService;
import QCraft.QCraft.service.GetAuthenticationService;
import QCraft.QCraft.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final ResumeFileTextRepository resumeFileTextRepository;
    private final ResumeFileRepository resumeFileRepository;
    private final InterviewRepository interviewRepository;
    private final ClaudeApiService claudeApiService;
    private final GetAuthenticationService getAuthenticationService;

    @Override
    public ResponseEntity<? super CreateInterviewResponseDTO> generateInterviewQuestions(String resumeFileId) {
        try {
            Optional<ResumeFile> resumeFileOptional = resumeFileRepository.findById(resumeFileId);
            if (resumeFileOptional.isEmpty()) {
                return GetFileResponseDTO.fileNotFound();
            }

            Optional<ResumeFileText> resumeFileTextOptional = resumeFileTextRepository.findByResumeFile(resumeFileOptional.get());
            if (resumeFileTextOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }

            List<String> question = claudeApiService.getInterviewQuestions(resumeFileTextOptional.get().getContent());

            List<String> questions = new ArrayList<>();
            for(String q : question){
                String[] splitByNewLine = q.split("\n");
                questions.addAll(Arrays.asList(splitByNewLine));
            }

            Interview interview = new Interview();
            interview.setQuestions(questions);
            interview.setCreatedAt(LocalDateTime.now());
            interview.setMember(getAuthenticationService.getAuthentication().get());
            interview.setResumeFile(resumeFileOptional.get());

            interviewRepository.save(interview);

            return CreateInterviewResponseDTO.success(interview);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetFeedbackResponseDTO> getFeedback(GetFeedbackRequestDTO requestDTO) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(requestDTO.getInterviewId());
            if (interviewOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }
            Interview interview = interviewOptional.get();

            List<String> feedback = claudeApiService.getFeedbackForAnswers(interview.getQuestions(), requestDTO.getAnswers());

            interview.setAnswers(requestDTO.getAnswers());
            interview.setFeedback(feedback);
            interviewRepository.save(interview);

            return GetFeedbackResponseDTO.success(interview);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetInterviewListResponseDTO> getInterviewList() {
        try {
            Optional<List<Interview>> interviewListOptional = interviewRepository.findInterviewsByMember(getAuthenticationService.getAuthentication().get());
            if (interviewListOptional.isEmpty()) {
                return GetInterviewListResponseDTO.interviewNotFound();
            }
            List<Interview> interviewList = interviewListOptional.get();

            return GetInterviewListResponseDTO.success(interviewList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetInterviewResponseDTO> getInterview(String interviewId) {
        try{
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isEmpty()) {
                return GetInterviewResponseDTO.interviewNotFound();
            }
            Interview interview = interviewOptional.get();

            return GetInterviewResponseDTO.success(interview);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super DeleteInterviewDTO> deleteInterview(String interviewId) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isEmpty()) {
                return DeleteInterviewDTO.InterviewNotFound();
            }

            Interview interview = interviewOptional.get();
            interviewRepository.delete(interview);
            return DeleteInterviewDTO.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }


}