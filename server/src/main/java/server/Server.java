package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.JoinRequest;
import datamodel.UserData;
import exception.ServerException;
import io.javalin.*;
import io.javalin.http.Context;
import service.AdminService;
import service.GameService;
import service.UserService;
import java.util.Map;

public class Server {

    private final Javalin server;

    private final UserService userService;
    private final GameService gameService;
    private final AdminService adminService;

    public Server() {
//        DataAccess dataAccess = new MemoryDataAccess();
        DataAccess dataAccess = new MySqlDataAccess();
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
        adminService = new AdminService(dataAccess);
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

    private void clear(Context ctx) throws Exception {
        adminService.clear();
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
        var req = ctx.header("Authorization");
        userService.logout(req);
        ctx.status(200);
        ctx.result("{}");
    }

    private void listGames(Context ctx) throws Exception {
        var serializer = new Gson();
        var req = ctx.header("Authorization");
        var res = gameService.listGames(req);
        ctx.status(200);
        ctx.result(serializer.toJson(Map.of("games", res)));
    }

    private void createGame(Context ctx) throws Exception {
        var serializer = new Gson();
        var auth = ctx.header("Authorization");
        var name = serializer.fromJson(ctx.body(), GameData.class);
        var gameID = gameService.createGame(auth, name.gameName());
        ctx.status(200);
        ctx.result(serializer.toJson(Map.of("gameID", gameID)));
    }

    private void joinGame(Context ctx) throws Exception {
        var serializer = new Gson();
        var auth = ctx.header("Authorization");
        var req = serializer.fromJson(ctx.body(), JoinRequest.class);
        gameService.joinGame(auth, req.playerColor(), req.gameID());
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
