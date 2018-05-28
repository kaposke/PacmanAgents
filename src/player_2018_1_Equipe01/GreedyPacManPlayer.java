package player_2018_1_Equipe01;

import javafx.beans.binding.DoubleExpression;
import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;
import player.DFSPacManPlayer;
import util.Utils;

import java.util.List;

public class GreedyPacManPlayer extends DFSPacManPlayer {

    Move lastMove = null;

    @Override
    public Move chooseMove(Game game) {
        State s = game.getCurrentState();
        List<Move> legalMoves = game.getLegalPacManMoves();

        Move bestMove = legalMoves.get(0);
        double bestEvaluation = Double.POSITIVE_INFINITY;

        // Pega o melhor movimento (com o evaluateState() mais alto)
        for (Move move : legalMoves) {
            if(move.equals(Move.NONE))
                continue;

            /**
             * Se quiser remover a opção pro pacman voltar, descomente esse codigo e comente o current evaluation de baixo.
             * Ele analisa o estado até chegar a uma intercecção.
             * Explica melhor no slide
             * e no SimplePacManPlayer
             **/
//            List<State> projectedStates = Game.getProjectedStates(s, move);
//            State last = projectedStates.get(projectedStates.size() - 1);
//            double currentEvaluation = evaluateState(last);

            // Com esse o pacman pode voltar
            double currentEvaluation = evaluateState(Game.getNextState(s, move));

            // Penalidade se ele quiser voltar (pra diminuir a ocorrencia de loops de vai e volta)
            if(move.getOpposite().equals(lastMove))
                currentEvaluation += Math.abs(currentEvaluation) * 0.9f;

            if(currentEvaluation < bestEvaluation) {
                bestMove = move;
                bestEvaluation = currentEvaluation;
            }
        }

        lastMove = bestMove;
        return bestMove;
    }

    @Override
    public double evaluateState(State next) {
        if(Game.isLosing(next))
            return Double.POSITIVE_INFINITY;
        if(Game.isWinning(next))
            return Double.NEGATIVE_INFINITY;

        Location location = next.getPacManLocation();

        int dotsAmount = next.getDotLocations().list().size();
        double closestDotDistance = Location.manhattanDistanceToClosest(location, next.getDotLocations().list());
        double mediumDotsDistance = getMediumDistance(location, next.getDotLocations().list());
        double closestGhostDistance = Location.manhattanDistanceToClosest(location, next.getGhostLocations());
        double mediumGhostsDistance = getMediumDistance(location, next.getGhostLocations());

        /**
         * Heuristica
         * Dica: Subtrair coisas que são <Quanto maior melhor>
         *       Somar coisas que são <Quanto menor melhor>
         * Ex: quanto menos pontos no mapa melhor. Por isso "+dotsAmount"
         *     quanto mais longe os fantasmas melhor. Por isso "-closestGhostDistance"
         * As multiplicações são um bom jeito de adicionar PESO aos fatores
         * Laroske <3
         * **/
//        double h = -dotsAmount - closestDotDistance + closestGhostDistance + mediumGhostsDistance;
//        double h = -dotsAmount - closestDotDistance + closestGhostDistance;
        double h = dotsAmount + closestDotDistance + mediumDotsDistance - closestGhostDistance;

        return h;
    }

    private double getMediumDistance(Location pacman, List<Location> dots) {
        double totalDistance = 0;
        for (Location dot : dots) {
            totalDistance += Location.manhattanDistance(pacman, dot);
        }
        return totalDistance / dots.size();
    }
}
