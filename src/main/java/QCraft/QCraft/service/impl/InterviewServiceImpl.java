package QCraft.QCraft.service.impl;

import QCraft.QCraft.MongoDBProjection.InterviewForListProjection;
import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.domain.Member;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final ResumeFileTextRepository resumeFileTextRepository;
    private final ResumeFileRepository resumeFileRepository;
    private final InterviewRepository interviewRepository;
    private final ClaudeApiService claudeApiService;
    private final GetAuthenticationService getAuthenticationService;

    //질문생성
    @Override
    public ResponseEntity<? super CreateInterviewResponseDTO> generateInterviewQuestions(String resumeFileId) {
        try {
            Optional<ResumeFile> resumeFileOptional = resumeFileRepository.findById(resumeFileId);
            if (resumeFileOptional.isEmpty()) {
                return GetFileResponseDTO.fileNotFound();
            }

            Optional<ResumeFileText> resumeFileTextOptional = resumeFileTextRepository.findByResumeFile_Id(resumeFileId);
            if (resumeFileTextOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }

            Optional<Interview> interviewOptional = interviewRepository.findByResumeFile_Id(resumeFileId);
            if (interviewOptional.isPresent()) {
                return CreateInterviewResponseDTO.failCreate();
            }

            List<String> question = claudeApiService.getInterviewQuestions(resumeFileTextOptional.get().getContent());

            List<String> questions = new ArrayList<>();
            for (String q : question) {
                String[] splitByNewLine = q.split("\n\n");
                questions.addAll(Arrays.asList(splitByNewLine));
            }

            Optional<Member> memberOptional = getAuthenticationService.getAuthentication();
            if (memberOptional.isEmpty()) {
                return ResponseDTO.validationError();
            }
            Member member = memberOptional.get();
            ResumeFile resumeFile = resumeFileOptional.get();
            ResumeFile nestedResumeFile = new ResumeFile(resumeFileId, resumeFile.getFilename(), resumeFile.getPath(), resumeFile.getExtension(), resumeFile.getUploadDate(), resumeFile.getMemberId());


            Interview interview = new Interview();
            interview.setQuestions(questions);
            interview.setCreatedAt(LocalDateTime.now());
            interview.setMemberId(member.getId());
            interview.setResumeFile(nestedResumeFile);

            interviewRepository.save(interview);

            return CreateInterviewResponseDTO.success(interview);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    //피드백 받기
    @Override
    public ResponseEntity<? super GetFeedbackResponseDTO> getFeedback(GetFeedbackRequestDTO requestDTO) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(requestDTO.getInterviewId());
            if (interviewOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }
            Interview interview = interviewOptional.get();

            String feedback = claudeApiService.getFeedbackForAnswers(interview.getQuestions(), requestDTO.getAnswers());

            FeedbackResult feedbackResult = parseFeedback(feedback);

            interview.setAnswers(requestDTO.getAnswers());
            interview.setPositivePoint(feedbackResult.getPositivePoint());
            interview.setImprovement(feedbackResult.getImprovement());
            interview.setOverallSuggestion(feedbackResult.getOverallSuggestion());
            interviewRepository.save(interview);

            return GetFeedbackResponseDTO.success(interview);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    //인터뷰 리스트 불러오기
    @Override
    public ResponseEntity<? super GetInterviewListResponseDTO> getInterviewList(int page, int limit, String direction) {
        try {
            Sort.Direction sortDirection = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(sortDirection, "createdAt");

            Pageable pageable = PageRequest.of(page, limit, sort);

            Optional<Member> memberOptional = getAuthenticationService.getAuthentication();
            if (memberOptional.isEmpty()) {
                return ResponseDTO.databaseError();
            }
            Member member = memberOptional.get();

            Page<InterviewForListProjection> interviewPage = interviewRepository.findByMemberId(member.getId(), pageable);

            if (page >= interviewPage.getTotalPages()) {
                return GetInterviewListResponseDTO.pageRangeOver();
            }

            if (interviewPage.isEmpty()) {
                return GetInterviewListResponseDTO.interviewNotFound();
            }

            List<InterviewSummaryDTO> interviewSummaryDTOList = interviewPage.getContent().stream()
                    .map(interview -> {
                        String id = interview.getId();
                        LocalDateTime createdAt = interview.getCreatedAt();
                        String resumeFilename = interview.getResumeFile().getFilename();
                        return new InterviewSummaryDTO(id, createdAt, resumeFilename);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));

            long totalInterviews = interviewPage.getTotalElements();

            return GetInterviewListResponseDTO.success(interviewSummaryDTOList, totalInterviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    //인터뷰 상세 불러오기
    @Override
    public ResponseEntity<? super GetInterviewResponseDTO> getInterview(String interviewId) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isEmpty()) {
                return GetInterviewResponseDTO.interviewNotFound();
            }
            Interview interview = interviewOptional.get();

            return GetInterviewResponseDTO.success(interview);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    //인터뷰 삭제
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }


    private FeedbackResult parseFeedback(String response) {
        List<List<String>> positivePoint = new ArrayList<>();
        List<List<String>> improvement = new ArrayList<>();
        String overall = "";

        String[] responseBlocks = response.split("전반적인 제언");

        if (responseBlocks.length > 0) {
            String feedbackPart = responseBlocks[0];
            String[] answerFeedbackBlocks = feedbackPart.split("답변 \\d+에 대한 피드백");

            for (int i = 1; i < answerFeedbackBlocks.length; i++) {
                String feedbackText = answerFeedbackBlocks[i].trim();

                List<String> positivePoints = extractPoints(feedbackText, "긍정적 측면:");
                positivePoint.add(positivePoints);

                List<String> improvementPoints = extractPoints(feedbackText, "개선이 필요한 부분:");
                improvement.add(improvementPoints);
            }
        }

        // 전반적인 제언 부분 파싱
        if (responseBlocks.length > 1) {
            overall = responseBlocks[1].trim();
        }

        return new FeedbackResult(positivePoint, improvement, overall);
    }

    private List<String> extractPoints(String text, String sectionTitle) {
        int sectionStartIndex = text.indexOf(sectionTitle);
        if (sectionStartIndex == -1) {
            return Collections.emptyList(); // 섹션이 없을 경우 빈 리스트 반환
        }

        String sectionText = text.substring(sectionStartIndex + sectionTitle.length()).trim();
        String[] lines = sectionText.split("\\n");

        List<String> points = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            if (line.matches("^\\d+\\.\\s.*")) { // "1. " 같은 형식으로 시작하는 라인만 가져오기
                points.add(line.substring(2).trim());
            } else {
                break; // 다음 섹션으로 넘어가기 전까지만 포함
            }
        }

        return points;
    }


}
