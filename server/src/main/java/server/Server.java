package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.GameData;
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

        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.get("game", this::listGames);
        server.post("game", this::createGame);

//        server.exception(ServerException.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

//    private void exceptionHandler(ServerException ex, Context ctx) {
//        ctx.status();
//        ctx.result(ex.toJson());
//    }

    private void register(Context ctx) throws Exception {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), UserData.class);
        if (req.username() == null|| req.email() == null || req.password() == null) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", "Error: bad request")));
            return;
        }
        AuthData res;
        try {
            res = userService.register(req);
            var regResult = serializer.toJson(res);
            ctx.result(regResult);
        } catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(ex.toJson());
        }
    }

    private void login(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), UserData.class);
        if (req.username() == null|| req.password() == null) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", "Error: bad request")));
            return;
        }
        AuthData res;
        try {
            res = userService.login(req);
            var loginResult = serializer.toJson(res);
            ctx.result(loginResult);
        } catch (UnauthorizedException ex) {
            ctx.status(401);
            ctx.result(ex.toJson());
        }
    }

    private void logout(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.header("Authorization"), String.class);
        try {
            userService.logout(req);
            ctx.status(200);
            ctx.result("{}");
        } catch (UnauthorizedException ex) {
            ctx.status(401);
            ctx.result(ex.toJson());
        }
    }

    private void listGames(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.header("Authorization"), String.class);
        try {
            var res = userService.listGames(req);
            ctx.status(200);
            ctx.result(serializer.toJson(Map.of("games", res)));
        } catch(UnauthorizedException ex) {
            ctx.status(401);
            ctx.result(ex.toJson());
        }
    }

    private void createGame(Context ctx) {
        var serializer = new Gson();
        var auth = serializer.fromJson(ctx.header("Authorization"), String.class);
        var name = serializer.fromJson(ctx.body(), GameData.class);
        if (name.gameName() == null) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", "Error: bad request")));
            return;
        }
        try {
            var gameID = userService.createGame(auth, name.gameName());
            ctx.status(200);
            ctx.result(serializer.toJson(Map.of("gameID", gameID)));
        } catch(UnauthorizedException ex) {
            ctx.status(401);
            ctx.result(ex.toJson());
        }
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
