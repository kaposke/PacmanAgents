package player_2018_1_Equipe01;

import pacman.Location;
import pacman.Move;
import pacman.State;

import java.util.List;

public interface Search {
    Move getBestMove(State start, int depth);

    double getHeuristic(State s);
}
