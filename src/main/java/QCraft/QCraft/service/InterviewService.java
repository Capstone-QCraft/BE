package QCraft.QCraft.service;

import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.dto.request.interview.GetFeedbackRequestDTO;
import QCraft.QCraft.dto.response.interview.*;
import org.springframework.http.ResponseEntity;

public interface InterviewService {
    ResponseEntity<? super CreateInterviewResponseDTO> generateInterviewQuestions(String resumeFileId);
    ResponseEntity<? super GetFeedbackResponseDTO> getFeedback(GetFeedbackRequestDTO requestDTO);
    ResponseEntity<? super GetInterviewListResponseDTO> getInterviewList();
    ResponseEntity<? super GetInterviewResponseDTO>getInterview(String interviewId);
    ResponseEntity<? super DeleteInterviewDTO>deleteInterview(String interviewId);
}
