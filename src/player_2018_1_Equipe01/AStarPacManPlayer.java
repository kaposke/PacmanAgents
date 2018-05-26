package player_2018_1_Equipe01;

import pacman.Game;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;

import java.util.List;

public class AStarPacManPlayer extends DFSPacManPlayer {

    @Override
    public Move chooseMove(Game game) {
        State s = game.getCurrentState();
        AStar aStar = new AStar();
        List<Move> possibleMoves = Game.getLegalPacManMoves(s);
        State bestState = aStar.getBestState(s, 500);
        for (Move move : possibleMoves){
            if(Game.getNextState(s, move).equals(bestState)) {
                System.out.println("Found CORRECT MOVE FOR BEST STATE");
                return move;
            }
        }
        System.out.println("Performing RANDOM move");
        return possibleMoves.get(0);
    }
}
