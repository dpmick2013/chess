package exception;

public class UnauthorizedException extends ServerException {
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}
