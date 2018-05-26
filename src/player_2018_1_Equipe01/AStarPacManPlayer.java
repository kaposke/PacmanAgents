package player_2018_1_Equipe01;

import pacman.Game;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

import java.util.List;

public class AStarPacManPlayer extends DFSPacManPlayer {

    private AStar aStar = new AStar();
    @Override
    public Move chooseMove(Game game) {
        State s = game.getCurrentState();
        return aStar.getBestMove(s,200);
    }
}
