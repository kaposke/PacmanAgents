package player_2018_1_Equipe01;

import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AStarSearch implements Search{

    private Move previousMove = Move.NONE;
    int currentDepth = 0;

    @Override
    public Move getBestMove(State start, int depth) {
        Node startNode = new Node(start, previousMove);
        startNode.setG(0);
        startNode.setH(getHeuristic(startNode.getState()));

        // Nós a serem explorados
        List<Node> openSet = new ArrayList<>();
        // Nós que já foram explorados
        List<Node> closedSet = new ArrayList<>();

        openSet.add(startNode);

        Node bestNode = new Node(start, Move.NONE);
        currentDepth = 0;
        // Enquanto ainda ouverem nós não explorados
        while (!openSet.isEmpty()) {
            // Pega o nó com menor custo
            Node current = openSet.get(0);
            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).getF() < current.getF() || openSet.get(i).getF() == current.getF() && openSet.get(i).getH() < current.getH())
                    current = openSet.get(i);
            }
            // Salvando o melhor de todos os loops
            if (current != startNode)
                if (current.getF() < bestNode.getF() || current.getF() == bestNode.getF() && current.getH() < bestNode.getH())
                    bestNode = current;

            // Checamos se o nó atual atinge o objetivo ou a profundidade maxima
            if (Game.isWinning(bestNode.getState()) || currentDepth >= depth) {
                List<Node> path = SearchUtils.retracePath(startNode, bestNode);
                // retorna o estado melhor proximo estado
                previousMove = path.get(0).getMove();
                return previousMove;
            }

            // Estamos explorando o nó com menor custo, então colocamos ele no closed
            openSet.remove(current);
            closedSet.add(current);

            currentDepth++;

            if (Game.isFinal(current.getState()))
                break;

            for (Node child : SearchUtils.getChildren(current)) {
                // Se o nó já estiver explorado, ignoramos
                if (closedSet.contains(child))
                    continue;

                // Calcula os custos do nó
                double h = getHeuristic(child.getState());
                //Penalidade por voltar
                if (child.getMove().getOpposite().equals(current.getMove()))
                    h = Math.abs(h) * 5;
                child.setH(h);
                child.setG(current.getG() + 1);
                child.setFather(current);
                // Se não adicionamos aos descobertos
                if (!openSet.contains(child))
                    openSet.add(child);
            }
        }

        System.out.println("Failed to find best Node.");
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
