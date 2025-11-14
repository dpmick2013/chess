package exception;

public class AlreadyTakenException extends ServerException {
    public AlreadyTakenException(String message) {
        super(message, 403);
    }
}
