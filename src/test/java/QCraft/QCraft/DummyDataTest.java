package QCraft.QCraft;

import QCraft.QCraft.domain.Interview;
import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.repository.InterviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
public class DummyDataTest {

    @Autowired
    private InterviewRepository interviewRepository;

    @Test
    public void insertDummyInterviews(){
        List<Interview> dummyInterviews = new ArrayList<>();

        for(int i= 1; i<=20;i++){
            Interview interview = new Interview();
            interview.setQuestions(List.of("Question 1", "Question 2"));
            interview.setAnswers(List.of("Answer 1", "Answer 2"));
            interview.setPositivePoint(List.of(List.of("Good point 1", "Good point 2")));
            interview.setImprovement(List.of(List.of("Improvement 1", "Improvement 2")));
            interview.setOverallSuggestion("This is the overall suggestion for interview " + i);
            interview.setCreatedAt(LocalDateTime.now().minusDays(i));
            interview.setMemberId("67331728e6d89c4bc3650f08");
            ResumeFile resumeFile = new ResumeFile();
            resumeFile.setFilename("testUUID_resume-" + i + ".pdf");
            resumeFile.setPath("dummy/path/testUUID_resume-" + i + ".pdf");
            resumeFile.setExtension("pdf");
            resumeFile.setUploadDate(LocalDateTime.now().minusDays(i));
            resumeFile.setMemberId("67331728e6d89c4bc3650f08");
            interview.setResumeFile(resumeFile);

            dummyInterviews.add(interview);
        }

        interviewRepository.saveAll(dummyInterviews);
    }
}
