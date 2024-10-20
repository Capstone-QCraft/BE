package QCraft.QCraft.commons;

public interface ResponseCode {
    String SUCCESS = "SU";

    String VALIDATION_FAILED = "VF";
    String DUPLICATE_EMAIL = "DE";
    String PASSWORD_MISMATCH = "PWM";

    String SIGN_IN_FAILED = "SF";
    String CERTIFICATE_EXPIRED = "CE";

    String MAIL_FAILED = "MF";
    String DATABASE_ERROR = "DBE";

    String EXPIRED_TOKEN = "ET";

    String FILE_ERROR = "FIE";
    String FILE_NOT_FOUND = "FIN";

}
