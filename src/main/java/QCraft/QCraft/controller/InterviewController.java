package QCraft.QCraft.controller;

import QCraft.QCraft.dto.request.interview.GetFeedbackRequestDTO;
import QCraft.QCraft.dto.response.interview.*;
import QCraft.QCraft.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interview")
public class InterviewController {
    private final InterviewService interviewService;

    //질문 생성
    @PostMapping("/generate")
    public ResponseEntity<? super CreateInterviewResponseDTO> generateInterviewQuestions(@RequestParam("resumeFileId") @Valid String resumeFileId) {
        return interviewService.generateInterviewQuestions(resumeFileId);
    }

    //피드백 받기
    @PostMapping("/feedback")
    public ResponseEntity<? super GetFeedbackResponseDTO> getFeedbackForAnswers(@RequestBody @Valid GetFeedbackRequestDTO requestDTO) {
        return interviewService.getFeedback(requestDTO);
    }

    //리스트
    @GetMapping("/list")
    public ResponseEntity<? super GetInterviewListResponseDTO> getInterviewList(@RequestParam("page") @Valid Integer page, @RequestParam("size") @Valid Integer size, @RequestParam(defaultValue = "DESC") @Valid String direction) {
        return interviewService.getInterviewList(page, size, direction);
    }

    //기록
    @GetMapping("/{interviewId}")
    public ResponseEntity<? super GetInterviewResponseDTO> getInterview(@PathVariable String interviewId) {
        return interviewService.getInterview(interviewId);
    }

    //삭제
    @DeleteMapping("/delete/{interviewId}")
    public ResponseEntity<? super DeleteInterviewDTO> deleteInterview(@PathVariable String interviewId) {
        return interviewService.deleteInterview(interviewId);
    }

}
