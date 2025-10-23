package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.JoinRequest;
import datamodel.UserData;
import exception.ServerException;
import exception.UnauthorizedException;
import io.javalin.*;
import io.javalin.http.Context;
import exception.AlreadyTakenException;
import service.UserService;
import java.util.Map;

public class Server {

    private final Javalin server;

    private final UserService userService;
    private final DataAccess dataAccess;

    public Server() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", this::clear);
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.get("game", this::listGames);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
        server.exception(ServerException.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    private void exceptionHandler(ServerException ex, Context ctx) {
        ctx.status(ex.getCode());
        ctx.result(ex.toJson());
    }

    private void clear(Context ctx) {
        userService.clear();
        ctx.status(200);
        ctx.result("{}");
    }

    private void register(Context ctx) throws Exception {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), UserData.class);
        var res = userService.register(req);
        var regResult = serializer.toJson(res);
        ctx.status(200);
        ctx.result(regResult);

    }

    private void login(Context ctx) throws Exception {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), UserData.class);
        AuthData res = userService.login(req);
        var loginResult = serializer.toJson(res);
        ctx.status(200);
        ctx.result(loginResult);
    }

    private void logout(Context ctx) throws Exception {
        var serializer = new Gson();
        var req = ctx.header("Authorization");
        userService.logout(req);
        ctx.status(200);
        ctx.result("{}");
    }

    private void listGames(Context ctx) throws Exception {
        var serializer = new Gson();
        var req = ctx.header("Authorization");
        var res = userService.listGames(req);
        ctx.status(200);
        ctx.result(serializer.toJson(Map.of("games", res)));
    }

    private void createGame(Context ctx) throws Exception {
        var serializer = new Gson();
        var auth = ctx.header("Authorization");
        var name = serializer.fromJson(ctx.body(), GameData.class);
        var gameID = userService.createGame(auth, name.gameName());
        ctx.status(200);
        ctx.result(serializer.toJson(Map.of("gameID", gameID)));
    }

    private void joinGame(Context ctx) throws Exception {
        var serializer = new Gson();
        var auth = ctx.header("Authorization");
        var req = serializer.fromJson(ctx.body(), JoinRequest.class);
        userService.joinGame(auth, req.playerColor(), req.gameID());
        ctx.status(200);
        ctx.result("{}");
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
