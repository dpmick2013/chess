package datamodel;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class GameList extends ArrayList<GameResult> {
    public GameList(Collection<GameResult> games) {
        super(games);
    }

    public String toString() {
        return new Gson().toJson(this.toArray());
    }
}
