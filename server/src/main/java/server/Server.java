package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.UserData;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin server;

    private final UserService userService;
    private DataAccess dataAccess;

    public Server() {
        dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", this::register);

        // Register your endpoints and exception handlers here.

    }

    private void register(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), UserData.class);
        AuthData res = null;
        try {
            res = userService.register(req);
        } catch (Exception e) {
//      "403\n{\"message\": \"Error: already taken\"}"
            ctx.status(403);
            ctx.result(serializer.toJson(Map.of("message", "Error: already taken")));
        }
        if (res != null) {
            var regResult = serializer.toJson(res);
            ctx.result(regResult);
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
