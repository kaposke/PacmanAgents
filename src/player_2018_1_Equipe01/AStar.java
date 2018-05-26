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

    private Move previousMove = Move.NONE;
    int currentDepth = 0;

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
        System.out.println("StartNode F: " + startNode.getF());
        currentDepth = 0;
        // Enquanto ainda ouverem nós não explorados
        while (!openSet.isEmpty()) {
            // Pega o nó com menor custo
            Node current = openSet.get(0);
            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).getF() < current.getF() || openSet.get(i).getF() == current.getF() && openSet.get(i).getH() < current.getH())
                    current = openSet.get(i);
            }
            System.out.println("Current Node F:" + current.getF());
            // Salvando o melhor de todos os loops
            if (current != startNode)
                if (current.getF() < bestNode.getF() || current.getF() == bestNode.getF() && current.getH() < bestNode.getH())
                    bestNode = current;

            // Checamos se o nó atual atinge o objetivo ou a profundidade maxima
            if (Game.isWinning(bestNode.getState()) || currentDepth >= depth) { // TODO: ANALISE
                List<Node> path = retracePath(startNode, bestNode);
                // retorna o estado melhor proximo estado
                previousMove = path.get(0).getMove();
                System.out.println("A* Successful! Going: " + previousMove);
                return previousMove;
            }

            // Estamos explorando o nó com menor custo, então colocamos ele no closed
            openSet.remove(current);
            closedSet.add(current);

            currentDepth++;

            if (Game.isFinal(current.getState()))
                break;

            // Vamos abrir o nó
            for (Node child : getChildren(current)) { // Pega todos os estados seguintes possiveis
                // Se o nó já estiver explorado, ignoramos
                if (closedSet.contains(child))
                    continue;
                // Se não adicionamos aos descobertos
                if (!openSet.contains(child))
                    openSet.add(child);
            }
        }

        System.out.println("Failed to find best Node.");
        return Move.NONE;
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
            Node nextNode = new Node(nextState, move);
            // Calcula os custos do nó
            double h = getHeuristic(nextState);
            //Penalidade por voltar
            if (move.getOpposite().equals(current.getMove()))
                h = Math.abs(h) * 5;
            nextNode.setH(h);
            nextNode.setG(current.getG() + 1);
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
        return dotsAmount * 3 + closestDotDistance + mediumDotsDistance * .1 - closestGhostDistance * .3;
    }

    private double getMediumDistance(Location pacman, List<Location> dots) {
        double totalDistance = 0;
        for (Location dot : dots) {
            totalDistance += Location.manhattanDistance(pacman, dot);
        }
        return totalDistance / dots.size();
    }
}
