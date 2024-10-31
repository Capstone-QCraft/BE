package QCraft.QCraft.service;

import QCraft.QCraft.domain.ResumeFile;
import QCraft.QCraft.domain.ResumeFileText;

public interface ResumeFileTextService {

    ResumeFileText createResumeFileText(ResumeFile resumeFile, String extension);
}
