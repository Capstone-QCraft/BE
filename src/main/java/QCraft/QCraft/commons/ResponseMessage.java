package QCraft.QCraft.commons;

public interface ResponseMessage {
    String SUCCESS = "success";

    String VALIDATION_FAILED = "validation failed";
    String DUPLICATE_EMAIL = "duplicate email";
    String PASSWORD_MISMATCH = "password mismatch";

    String SIGN_IN_FAILED = "sign in failed";
    String CERTIFICATE_EXPIRED = "certificate expired";

    String MAIL_FAILED = "mail failed";
    String DATABASE_ERROR = "database error";

    String EXPIRED_TOKEN = "expired token";

    String FILE_ERROR = "file error";
    String FILE_NOT_FOUND = "file not found";

    String QUESTION_GENERATION_FAILED = "question generation failed";
    String FEEDBACK_GENERATION_FAILED = "feedback generation failed";
    String CLAUDE_ERROR = "claude error";
    String INTERVIEW_NOT_FOUND = "interview not found";
}
