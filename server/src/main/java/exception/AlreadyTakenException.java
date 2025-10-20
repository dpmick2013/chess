package exception;

import com.google.gson.Gson;

import java.util.Map;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }
}
