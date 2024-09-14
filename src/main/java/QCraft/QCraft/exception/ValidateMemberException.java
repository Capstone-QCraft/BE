package QCraft.QCraft.exception;

public class ValidateMemberException extends RuntimeException {
    public ValidateMemberException(String message) {
        super(message);
    }

    public ValidateMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
