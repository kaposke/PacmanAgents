package player_2018_1_Equipe01;

import pacman.Game;
import pacman.Location;
import pacman.Move;
import pacman.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchUtils {

    static List<Node> retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != startNode) {
            path.add(current);
            current = current.getFather();
        }
        Collections.reverse(path);
        return path;
    }

    static List<Node> getChildren(Node current) {
        List<Move> possibleMoves = Game.getLegalPacManMoves(current.getState());
        List<Node> nodes = new ArrayList<>();
        for (Move move : possibleMoves) {
            // Pega o proximo nó
            State nextState = Game.getNextState(current.getState(), move);
            Node nextNode = new Node(nextState, move);
            // Adiciona á lista
            nodes.add(nextNode);
        }
        return nodes;
    }

    static double getMediumDistance(Location pacman, List<Location> dots) {
        double totalDistance = 0;
        for (Location dot : dots) {
            totalDistance += Location.manhattanDistance(pacman, dot);
        }
        return totalDistance / dots.size();
    }
}
