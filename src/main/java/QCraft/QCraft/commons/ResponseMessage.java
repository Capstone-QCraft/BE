package QCraft.QCraft.commons;

public interface ResponseMessage {
    String SUCCESS = "success";

    String VALIDATION_FAILED = "validation failed";
    String DUPLICATE_EMAIL = "duplicate email";

    String SIGN_IN_FAILED = "sign in failed";
    String CERTIFICATE_EXPIRED = "certificate expired";

    String MAIL_FAILED = "mail failed";
    String DATABASE_ERROR = "database error";
}
