package client;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;
import exception.ServerException;

import com.google.gson.Gson;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;

    public ServerFacade(String url) {
        serverURL = url;
    }

    public void clear() throws Exception {
        var request = buildRequest("DELETE", "/db", null, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public AuthData register(UserData user) throws Exception {
        var request = buildRequest("POST", "/user", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(UserData user) throws Exception {
        var request = buildRequest("POST", "/session", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public int createGame(String name, String token) throws Exception {
        var gameName = new GameData(null, null, null, name, null);
        var request = buildRequest("POST", "/game", gameName, token);
        var response = sendRequest(request);
        var result = handleResponse(response, CreateGameResponse.class);
        assert result != null;
        return result.gameID();
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object body) {
        if (body != null) {
            return BodyPublishers.ofString(new Gson().toJson(body));
        }
        else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ServerException {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body != null) {
                var map = new Gson().fromJson(body, HashMap.class);
                String message = map.get("message").toString();
                throw new ServerException(message, status);
            }
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }
}
