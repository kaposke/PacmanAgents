package player_2018_1_Equipe01;

import jdk.nashorn.api.tree.LiteralTree;
import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;

import java.util.*;

public class DepthSearch implements Search {
    Move previousMove = Move.NONE;

    @Override
    public Move getBestMove(State start, int depth) {
        Node startNode = new Node(start, previousMove);
        startNode.setG(0);
        startNode.setH(getHeuristic(startNode.getState()));

        Queue<Node> openSet = new LinkedList<>();
        List<Node> closedSet = new ArrayList<>();

        openSet.add(startNode);

        Node bestNode = new Node(start, Move.NONE);

        Queue<Node> nextLevel = new LinkedList<>();

        int currentDepth = 0;
        while (!openSet.isEmpty()) {

            Node current = openSet.remove();

            if (current != startNode)
                if (current.getF() < bestNode.getF() || current.getF() == bestNode.getF() && current.getH() < bestNode.getH())
                    bestNode = current;

            if (Game.isWinning(bestNode.getState()) || currentDepth >= depth) {
                List<Node> path = SearchUtils.retracePath(startNode, bestNode);
                // retorna o melhor proximo estado
                previousMove = path.get(0).getMove();
                return previousMove;
            }

            if (Game.isFinal(current.getState())) {
                System.out.println("Got into a final state");
                if(openSet.isEmpty()) {
                    List<Node> path = SearchUtils.retracePath(startNode, bestNode);
                    // retorna o melhor proximo estado
                    previousMove = path.get(0).getMove();
                    return previousMove;
                }
                continue;
            }
            System.out.println(".");
            for (Node child : SearchUtils.getChildren(current)) {
                // Calcula os custos do nó
                double h = getHeuristic(child.getState());
                //Penalidade por voltar
                if (child.getMove().getOpposite().equals(current.getMove()))
                    h = Math.abs(h) * 5;
                child.setH(h);
                child.setG(current.getG() + 1);
                child.setFather(current);
                // Se não adicionamos aos descobertos
                if (!closedSet.contains(child))
                    nextLevel.add(child);
            }

            closedSet.add(current);

            if (openSet.isEmpty()) {
                openSet.addAll(nextLevel);
                nextLevel.clear();
                currentDepth++;
            }
        }
        System.out.println("Failed to find best Node.");
        System.out.println("OpenSet Size: " + openSet.size());
        System.out.println("Best Node F: " + bestNode.getF());
        return Move.NONE;
    }

    @Override
    public double getHeuristic(State s) {
        if (Game.isLosing(s))
            return Double.POSITIVE_INFINITY;
        if (Game.isWinning(s))
            return Double.NEGATIVE_INFINITY;

        Location location = s.getPacManLocation();

        int dotsAmount = s.getDotLocations().list().size();
        double closestDotDistance = Location.manhattanDistanceToClosest(location, s.getDotLocations().list());
        double mediumDotsDistance = SearchUtils.getMediumDistance(location, s.getDotLocations().list());
        double closestGhostDistance = Location.manhattanDistanceToClosest(location, s.getGhostLocations());
        double mediumGhostsDistance = SearchUtils.getMediumDistance(location, s.getGhostLocations());

        /**
         * Heuristica
         * Dica: Subtrair coisas que são <Quanto maior melhor>
         *       Somar coisas que são <Quanto menor melhor>
         * Ex: quanto menos pontos no mapa melhor. Por isso "+dotsAmount"
         *     quanto mais longe os fantasmas melhor. Por isso "-closestGhostDistance"
         * Laroske <3
         * **/
        return dotsAmount * 3 + closestDotDistance + mediumDotsDistance * .1 - closestGhostDistance * .3;
    }
}
