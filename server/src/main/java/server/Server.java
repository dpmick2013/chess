package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.UserData;
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

        // Register your endpoints and exception handlers here.

    }

    private void register(Context ctx) {
        var serializer = new Gson();
        var req = serializer.fromJson(ctx.body(), UserData.class);
        if (req.username() == null || req.email() == null || req.password() == null) {
            ctx.status(400);
            ctx.result(serializer.toJson(Map.of("message", "Error: bad request")));
            return;
        }
        AuthData res = null;
        try {
            res = userService.register(req);
        } catch (AlreadyTakenException ex) {
            ctx.status(403);
            ctx.result(ex.toJson());
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
