package exception;

import com.google.gson.Gson;

import java.util.Map;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException() {}

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", 403));
    }
}
