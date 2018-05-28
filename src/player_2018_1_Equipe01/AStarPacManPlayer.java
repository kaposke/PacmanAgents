package player_2018_1_Equipe01;

import pacman.Game;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

public class AStarPacManPlayer extends DFSPacManPlayer {

    private AStarSearch aStar = new AStarSearch();
    @Override
    public Move chooseMove(Game game) {
        State s = game.getCurrentState();
        return aStar.getBestMove(s,200);
    }
}
