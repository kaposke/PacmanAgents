package player_2018_1_Equipe01;

import pacman.State;

public class Node {
    private Node father;

    private State state;
    private double g = Double.POSITIVE_INFINITY;
    private double h = Double.POSITIVE_INFINITY;

    public Node(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return getG() + getH();
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public Node getFather() {
        return father;
    }
}
