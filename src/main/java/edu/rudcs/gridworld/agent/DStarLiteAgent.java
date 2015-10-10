package edu.rudcs.gridworld.agent;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.PriorityBlockingQueue;

import edu.rudcs.gridworld.map.ActorType;
import edu.rudcs.gridworld.map.actor.Wall;
import edu.rudcs.gridworld.util.Cost;
import edu.rudcs.gridworld.util.Tool;

public class DStarLiteAgent extends Agent {

    private State[][] map;
    private Queue<State> queueU;
    private int rows;
    private int cols;
    private Location curr;
    private Location goal;
    private Cost km;
    private boolean isStart;

    public DStarLiteAgent(Location goal) {
        this.goal = goal;
        this.isStart = false;
    }

    @Override
    public void act() {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }

        if (!isStart) {
            isStart = true;
            curr = getLocation();
            rows = grid.getNumRows();
            cols = grid.getNumCols();
            map = new State[rows][cols];
            queueU = new PriorityBlockingQueue<State>();
            initialize();
            computeShortestPath();
            tracePath();
            return;
        }

        if (!end || curr.equals(goal)) {
            end = true;
            return;
        }

        State next = getState(curr).next;
        Actor actor = grid.get(next.loc);
        if (actor instanceof Wall) {
            next.type = ActorType.WALL;
            km = km.add(getHeuristic(curr, next.loc));
            List<State> neighbors = getNeighbors(next);
            for (State n : neighbors) {
                updateVertex(n);
            }
            computeShortestPath();
            tracePath();
        } else {
            moveTo(next.loc);
            // lastState = stateList.getState(start);
            curr = next.loc;
        }
    }

    private State getState(Location loc) {
        if (map[loc.getRow()][loc.getCol()] == null) {
            map[loc.getRow()][loc.getCol()] = new State(loc);
        }
        return map[loc.getRow()][loc.getCol()];
    }

    private void tracePath() {
        //info("#FUNCTION# TracePath");
        // TODO how to check infinite loop?
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        grid.resetColors();
        State currState = getState(curr);
        Set<State> visited = new TreeSet<State>();
        //info(prtMap());
        while (!currState.loc.equals(goal)) {
            //info("%s", currState);
            grid.putColor(currState.loc, PATH_COLOR);
            currState.next = null;
            Cost nextCost = Cost.INFINITE;
            List<State> succs = getSuccs(currState);
            for (State s : succs) {
                if (visited.contains(s)) {
                    continue;
                }
                Cost tmp = getCost(currState, s).add(s.g);
                if (currState.next == null || nextCost.gt(tmp)) {
                    currState.next = s;
                    nextCost = tmp;
                }
            }
            visited.add(currState);
            currState = currState.next;
        }
        prtPath();
        grid.putColor(currState.loc, PATH_COLOR);
    }

    private void prtPath() {
        StringBuffer sb = new StringBuffer("Find path: \n");
        State currState = getState(curr);
        while (currState.next != null) {
            sb.append(String.format("%s-[%s]->", currState.loc,
                    getCost(currState, currState.next).add(currState.next.g)));
            currState = currState.next;
        }
        sb.append(String.format("%s:GOAL", currState.loc));
        //info(sb);
    }

    public Key calculateKey(State s) {
        // {01}
        Cost k2 = Cost.min(s.g, s.rhs);
        Cost k1 = k2.add(s.h).add(km);
        return new Key(k1, k2);
    }

    public void initialize() {
        //info("#FUNC START# initialize()");
        // {02}
        queueU.clear();
        // {03}
        km = new Cost("0.0");
        // {05}
        State goalState = getState(goal);
        goalState.rhs = new Cost(0);
        // {06}
        goalState.k = calculateKey(goalState);
        queueU.add(goalState);
        //info("#FUNC END# initialize()");
    }

    public void updateVertex(State state) {
        //info("#FUNC START# updateVertex(%s)", state);
        // {07}
        if (!state.loc.equals(goal)) {
            state.rhs = Cost.INFINITE;
            for (State s : getSuccs(state)) {
                Cost tmp = getCost(state, s).add(s.g);
                if (state.rhs.gt(tmp)) {
                    state.rhs = tmp;
                }
            }
        }
        // {08}
        queueU.remove(state);
        // {09}
        if (state.g.neq(state.rhs)) {
            state.k = calculateKey(state);
            queueU.add(state);
        }
        //info("#FUNC END# updateVertex(%s)", state);
    }

    public void computeShortestPath() {
        //info("#FUNC START# computeShortestPath()");
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }

        // {10}
        State top = queueU.peek();
        State startState = getState(curr);
        while (top != null && top.k.compareTo(calculateKey(startState)) < 0
                || startState.rhs.neq(startState.g)) {
            // {11}
            Key kOld = top.k;
            // {12}
            top = queueU.poll();
            // {13}
            if (kOld.compareTo(calculateKey(top)) < 0) {
                // {14}
                top.k = calculateKey(top);
                queueU.add(top);
            }
            // {15}
            else if (top.g.gt(top.rhs)) {
                // {16}
                top.g = top.rhs;
                // {17}
                for (State p : getPreds(top)) {
                    updateVertex(p);
                }
            }
            // {18}
            else {
                // {19}
                top.g = Cost.INFINITE;
                // {20}
                updateVertex(top);
                for (State p : getPreds(top)) {
                    updateVertex(p);
                }
            }
        }
        //info("#FUNC END# computeShortestPath()");
    }

    private List<State> getNeighbors(Location loc) {
        Grid<Actor> grid = getGrid();
        List<State> neighbors = new ArrayList<State>();
        List<Location> locs = grid.getValidAdjacentLocations(loc);
        for (Location l : locs) {
            State s = getState(l);
            if (s.type != ActorType.WALL) {
                neighbors.add(s);
            }
        }
        return neighbors;
    }

    private List<State> getNeighbors(State state) {
        return getNeighbors(state.loc);
    }

    private List<State> getPreds(Location loc) {
        return getNeighbors(loc);
    }

    private List<State> getPreds(State state) {
        return getPreds(state.loc);
    }

    private List<State> getSuccs(Location loc) {
        return getNeighbors(loc);
    }

    private List<State> getSuccs(State state) {
        return getSuccs(state.loc);
    }

    private Cost getHeuristic(Location l1, Location l2) {
        return Tool.euclideanDist(l1, l2);
    }

    private Cost getHeuristic(State s1, State s2) {
        return Tool.euclideanDist(s1.loc, s2.loc);
    }

    private Cost getCost(State s1, State s2) {
        if (s1.type != ActorType.WALL && s2.type != ActorType.WALL) {
            return getHeuristic(s1, s2);
        }
        return Cost.INFINITE;
    }

    public String prtMap() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(getState(new Location(i, j)));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Class State
     */
    private class State implements Comparable<State> {
        private Location loc;
        private Cost g;
        private Cost rhs;
        private Cost h;
        private Key k;
        private State next;
        private ActorType type;

        public State(Location loc) {
            this.loc = loc;
            this.g = Cost.INFINITE;
            this.rhs = Cost.INFINITE;
            this.h = Tool.euclideanDist(loc, curr);
            this.next = null;
            this.type = ActorType.ROAD;
        }

        @Override
        public int compareTo(State s) {
            return this.k.compareTo(s.k);
        }

        @Override
        public boolean equals(Object o) {
            State s = (State) o;
            return this.loc.equals(s.loc);
        }

        @Override
        public int hashCode() {
            return this.loc.hashCode();
        }

        @Override
        public String toString() {
            return String.format("%s:{%s,%s,%s,%s} ", loc, g, rhs, h, type);
        }
    }

    /**
     * Class Key
     */
    private class Key implements Comparable<Key> {
        private Cost k1;
        private Cost k2;

        public Key(Cost k1, Cost k2) {
            this.k1 = k1;
            this.k2 = k2;
        }

        @Override
        public int compareTo(Key key) {
            int result = Cost.compare(this.k1, key.k1);
            if (result != 0) {
                return result;
            }
            return Cost.compare(this.k2, key.k2);
        }

        @Override
        public String toString() {
            return String.format("(k1=%s,k2=%s)", k1, k2);
        }
    }
}
