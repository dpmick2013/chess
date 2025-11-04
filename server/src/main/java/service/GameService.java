package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import datamodel.GameResult;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ArrayList<GameResult> listGames(String authToken) throws Exception {
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return dataAccess.getGameList();
    }

    public int createGame(String authToken, String name) throws Exception {
        if (name == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return dataAccess.createGame(name);
    }

    public void joinGame(String authToken, String color, Integer gameID) throws Exception {
        if (!Objects.equals(color, "BLACK") && !Objects.equals(color, "WHITE")) {
            throw new BadRequestException("Error: bad request");
        }
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        var game = dataAccess.getGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: game does not exist");
        }
        ChessGame.TeamColor playerColor = null;
        if (Objects.equals(color, "WHITE")) {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(color, "BLACK")){
            playerColor = ChessGame.TeamColor.BLACK;
        }
        if (dataAccess.getPlayer(playerColor, game) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        dataAccess.joinGame(playerColor, auth.username(), gameID);
    }
}
