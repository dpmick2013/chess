package exception;

import com.google.gson.Gson;

import java.util.Map;

public class ServerException extends Exception {
    private final int code;
    public ServerException(String message, int code) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }
}
