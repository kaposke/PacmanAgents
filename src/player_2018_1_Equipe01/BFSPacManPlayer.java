package player_2018_1_Equipe01;

import pacman.Game;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

public class BFSPacManPlayer extends DFSPacManPlayer {

    DepthSearch depthSearch = new DepthSearch();
    @Override
    public Move chooseMove(Game game) {
        State s = game.getCurrentState();
        return depthSearch.getBestMove(s, 5);
    }
}
