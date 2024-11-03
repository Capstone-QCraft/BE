package QCraft.QCraft.service;

import QCraft.QCraft.dto.request.interview.ClaudeRequestDTO;
import QCraft.QCraft.dto.response.interview.ClaudeResponseDTO;
import QCraft.QCraft.repository.InterviewRepository;
import QCraft.QCraft.repository.ResumeFileTextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClaudeApiService {
    private final String apiUrl;
    private final ResumeFileTextRepository resumeFileTextRepository;
    private final InterviewRepository interviewRepository;
    private final RestTemplate claudeRestTemplate;

    @Autowired
    public ClaudeApiService(RestTemplate claudeApiRestTemplate, @Value("${claude.api.url}") String apiUrl, ResumeFileTextRepository resumeFileTextRepository, InterviewRepository interviewRepository, RestTemplate claudeRestTemplate) {
        this.apiUrl = apiUrl;
        this.resumeFileTextRepository = resumeFileTextRepository;
        this.interviewRepository = interviewRepository;
        this.claudeRestTemplate = claudeRestTemplate;
    }

    public List<String> getInterviewQuestions(String resumeContent) {
        List<ClaudeRequestDTO.Message> messages = new ArrayList<>();

        String systemPrompt = "Generate three interview questions based on the given resume content. " +
                "Your task is to generate a series of thoughtful, open-ended questions for an interview based on the given context. " +
                "The questions should be designed to elicit insightful and detailed responses from the interviewee, allowing them to showcase their knowledge, experience, and critical thinking skills. " +
                "Avoid yes/no questions or those with obvious answers. " +
                "Instead, focus on questions that encourage reflection, self-assessment, and the sharing of specific examples or anecdotes. " +
                "Since the text extracted from the file is passed as is, do not consider useless content when generate a question. " +
                "Please provide each question separated by a newline (\\n). Generate in Korean.";


        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Text extracted from resume:\n");
        promptBuilder.append(resumeContent);
        messages.add(ClaudeRequestDTO.Message.builder()
                .role("user")
                .content(promptBuilder.toString())
                .build());

        ClaudeRequestDTO requestDTO = ClaudeRequestDTO.builder()
                .model("claude-3-5-sonnet-20241022")
                .maxTokens(500)
                .temperature(0.43)
                .system(systemPrompt)
                .messages(messages)
                .build();

        try {
            ClaudeResponseDTO responseDTO = claudeRestTemplate.postForObject(apiUrl, requestDTO, ClaudeResponseDTO.class);
            if (responseDTO != null && responseDTO.getContent() != null) {
                return responseDTO.getContent().stream()
                        .map(ClaudeResponseDTO.Content::getText)
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("Claude API returned an invalid response");
            }
        } catch (Exception e) {
            log.error("Error calling Claude API: ", e);
            throw new RuntimeException("Failed to generate interview questions", e);
        }
    }

    public List<String> getFeedbackForAnswers(List<String> questions, List<String> answers) {
        List<ClaudeRequestDTO.Message> messages = new ArrayList<>();

        String systemPrompt = "Please analyze the following interview questions and answers to provide feedback. " +
                "Do not give evaluation scores or anything like that."+
                "Provide detailed feedback focusing on clarity, content, and areas for improvement. Generate in Korean.";

        StringBuilder promptBuilder = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            promptBuilder.append(String.format("Question %d: %s\n", i + 1, questions.get(i).replace("\n","")));
            promptBuilder.append(String.format("Answer: %d: %s\n", i + 1, answers.get(i).replace("\n","")));
            promptBuilder.append("\n");
        }

        promptBuilder.append("Please evaluate the following aspects for each answer:\n");
        promptBuilder.append("1. Clarity and specificity of the response\n");
        promptBuilder.append("2. Inclusion of key points\n");
        promptBuilder.append("3. Areas for improvement\n");
        promptBuilder.append("4. Overall assessment\n");

        messages.add(ClaudeRequestDTO.Message.builder()
                .role("user")
                .content(promptBuilder.toString())
                .build());

        ClaudeRequestDTO requestDTO = ClaudeRequestDTO.builder()
                .model("claude-3-5-sonnet-20241022")
                .maxTokens(1000)
                .temperature(0.5)
                .system(systemPrompt)
                .messages(messages)
                .build();

        System.out.println(promptBuilder);

        try {
            ClaudeResponseDTO response = claudeRestTemplate.postForObject(apiUrl, requestDTO, ClaudeResponseDTO.class);
            if (response != null && response.getContent() != null) {
                return response.getContent().stream()
                        .map(ClaudeResponseDTO.Content::getText)
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("Claude API returned an invalid response");
            }
        } catch (RestClientException e) {
            log.error("Error calling Claude API: ", e);
            throw new RuntimeException("Failed to get feedback from Claude", e);
        }
    }
}
