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

public class AStar {

    public State getBestState(State start, int depth) {
        Node startNode = new Node(start);
        startNode.setG(0);
        startNode.setH(getHeuristic(startNode.getState()));

        // Nós a serem explorados
        List<Node> openSet = new ArrayList<>();
        // Nós que já foram explorados
        List<Node> closedSet = new ArrayList<>();

        openSet.add(startNode);

        int currentDepth = 0;
        // Enquanto ainda ouverem nós não explorados
        while (!openSet.isEmpty()) {
            // Pega o nó com menor custo
            Node current = openSet.get(0);
            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).getF() < current.getF() || openSet.get(i).getF() == current.getF() && openSet.get(i).getH() < current.getH())
                    current = openSet.get(i);
            }
            // Estamos explorando o nó com menor custo, então colocamos ele no closed
            openSet.remove(current);
            closedSet.add(current);

            currentDepth++;
            // Checamos se o nó atual atinge o objetivo ou a profundidade maxima
            if (Game.isWinning(current.getState()) || currentDepth >= depth) {
                System.out.println("A* Successful!");
                List<Node> path = retracePath(startNode, current);
                // retorna o estado melhor proximo estado
                return path.get(0).getState();
            }

            if(Game.isFinal(current.getState())) {
                break;
            }

            // Vamos abrir o nó
            for (Node child : getChildren(current)) { // Pega todos os estado seguintes possiveis
                // Se o nó já estiver explorado, ignoramos
                if (closedSet.contains(child))
                    continue;
                // Se não adicionamos aos descobertos
                if (!openSet.contains(child))
                    openSet.add(child);
            }
        }

        System.out.println("Failed to find best Node.");
        return start;
    }

    private List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != startNode) {
            path.add(current);
            current = current.getFather();
        }
        Collections.reverse(path);
        return path;
    }

    private List<Node> getChildren(Node current) {
        List<Move> possibleMoves = Game.getLegalPacManMoves(current.getState());
        List<Node> nodes = new ArrayList<>();
        for (Move move : possibleMoves) {
            // Pega o proximo nó
            State nextState = Game.getNextState(current.getState(), move);
            Node nextNode = new Node(nextState);
            // Calcula os custos do nó
            nextNode.setG(current.getG() + 1);
            nextNode.setH(getHeuristic(nextState));
            nextNode.setFather(current);
            // Adiciona á lista
            nodes.add(nextNode);
        }
        return nodes;
    }

    // Heuristica
    private double getHeuristic(State s) {
        if (Game.isLosing(s))
            return Double.POSITIVE_INFINITY;
        if (Game.isWinning(s))
            return Double.NEGATIVE_INFINITY;

        Location location = s.getPacManLocation();

        int dotsAmount = s.getDotLocations().list().size();
        double closestDotDistance = Location.manhattanDistanceToClosest(location, s.getDotLocations().list());
        double mediumDotsDistance = getMediumDistance(location, s.getDotLocations().list());
        double closestGhostDistance = Location.manhattanDistanceToClosest(location, s.getGhostLocations());
        double mediumGhostsDistance = getMediumDistance(location, s.getGhostLocations());

        /**
         * Heuristica
         * Dica: Subtrair coisas que são <Quanto maior melhor>
         *       Somar coisas que são <Quanto menor melhor>
         * Ex: quanto menos pontos no mapa melhor. Por isso "+dotsAmount"
         *     quanto mais longe os fantasmas melhor. Por isso "-closestGhostDistance"
         * Laroske <3
         * **/
        double h = dotsAmount + closestDotDistance;

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
