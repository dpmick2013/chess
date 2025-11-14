package client;

import datamodel.GameResult;
import java.util.ArrayList;

public record ListGameResponse(ArrayList<GameResult> games) {
}
