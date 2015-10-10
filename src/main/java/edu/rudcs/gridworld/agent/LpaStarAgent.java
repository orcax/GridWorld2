package edu.rudcs.gridworld.agent;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import edu.rudcs.gridworld.map.actor.Goal;
import edu.rudcs.gridworld.map.actor.Wall;
import edu.rudcs.gridworld.util.Cost;
import edu.rudcs.gridworld.util.Tool;

public class LpaStarAgent extends Agent {

    private EdgeCost edgeCost;
    private StateList stateList;
    private Queue<State> queueU;
    private List<State> path;

    private State startState;
    private Location goal;
    private State goalState;
    private boolean isStart;
    private boolean isEnd;
    private int step;

    public LpaStarAgent(Location goal) {
        this.edgeCost = new EdgeCost();
        this.stateList = new StateList();
        this.queueU = new PriorityBlockingQueue<State>();
        this.path = new ArrayList<State>();

        this.goal = goal;
        this.goalState = stateList.getState(goal);
        this.isStart = false;
        this.isEnd = false;
    }

    @Override
    public void act() {
        if (!isStart) {
            initialize();
            computeShortestPath();
            backTracePath();
            step = path.size() - 1;
            isStart = true;
        } else {
            if (isEnd) {
                return;
            }
            Grid<Actor> grid = getGrid();
            if (grid == null) {
                warn("Grid is null");
                return;
            }
            State nextState = path.get(step - 1);
            Actor actor = grid.get(nextState.loc);
            if (actor instanceof Goal) {
                moveTo(nextState.loc);
                isEnd = true;
            } else if (actor instanceof Wall) {
                info("#CONDITION# STUCK WITH WALL: " + nextState.loc);
                // {21}
                List<Location> neighbors = grid
                        .getValidAdjacentLocations(nextState.loc);
                for (Location loc : neighbors) {
                    // {22}
                    edgeCost.setCost(nextState.loc, loc, Cost.INFINITE);
                    // {23}
                    updateVertex(stateList.getState(loc));
                    updateVertex(nextState);
                }
                computeShortestPath();
                backTracePath();
                step = path.size() - 1;
            } else {
                info("#CONDITION# Move to " + nextState.loc);
                moveTo(nextState.loc);
                step--;
            }
        }
    }

    private void printQueue() {
        StringBuffer sb = new StringBuffer();
        for (State s : queueU) {
            sb.append(s + "\n");
        }
        info("Priority Queue:\n" + sb.toString());
    }

    private static LpaStarKey calculateKey(State s) {
        Cost k2 = Cost.min(s.g, s.rhs);
        Cost k1 = k2.add(s.h);
        return new LpaStarKey(k1, k2);
    }

    private void initialize() {
        // {02}
        queueU.clear();
        // {04}
        startState = stateList.getState(getLocation());
        startState.rhs = new Cost(0);
        // {05}
        startState.k = calculateKey(startState);
        queueU.add(startState);
    }

    private void updateVertex(State state) {
        info("#FUNCTION# UpdateVertex: " + state);
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        // {06}
        if (!state.equals(startState)) {
            List<Location> neighbors = grid
                    .getValidAdjacentLocations(state.loc);
            state.rhs = Cost.INFINITE;
            for (Location loc : neighbors) {
                State neighbor = stateList.getState(loc);
                /*
                if (neighbor.predecessor != null
                        && neighbor.predecessor.equals(state)) {
                    continue;
                }
                */
                Cost c = edgeCost.getCost(state.loc, loc);
                if (state.rhs.gt(neighbor.g.add(c))) {
                    state.rhs = neighbor.g.add(c);
                    state.predecessor = neighbor;
                }
            }
        }
        // {07}
        if (queueU.contains(state)) {
            queueU.remove(state);
        }
        // {08}
        if (!state.g.eq(state.rhs)) {
            state.k = calculateKey(state);
            queueU.add(state);
        }
        info("#RESULT# " + state);
    }

    private void computeShortestPath() {
        info("#FUNCTION# ComputeShortestPath");
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }

        // {09}
        State state = queueU.peek();
        while (state != null && state.k.compareTo(calculateKey(goalState)) < 0
                || !goalState.rhs.eq(goalState.g)) {
            // {10}
            state = queueU.poll();
            info("Processing " + state);
            // {11}
            if (state.g.gt(state.rhs)) {
                // {12}
                state.g = state.rhs;
                // {13}
                List<Location> neighbors = grid
                        .getValidAdjacentLocations(state.loc);
                for (Location loc : neighbors) {
                    State neighbor = stateList.getState(loc);
                    if (state.predecessor != null
                            && state.predecessor.equals(neighbor)) {
                        continue;
                    }
                    neighbor.predecessor = state;
                    updateVertex(neighbor);
                }
            }
            // {14}
            else {
                // {15}
                state.g = Cost.INFINITE;
                // {16}
                updateVertex(state);
                List<Location> neighbors = grid
                        .getValidAdjacentLocations(state.loc);
                for (Location loc : neighbors) {
                    State neighbor = stateList.getState(loc);
                    if (state.predecessor != null
                            && state.predecessor.equals(neighbor)) {
                        continue;
                    }
                    neighbor.predecessor = state;
                    updateVertex(neighbor);
                }
            }
        }
        info("#RESULT# State List:\n" + stateList);
        printQueue();
    }

    private void backTracePath() {
        info("#FUNCTION# BackTracePath");
        // TODO how to check infinite loop?
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        path.clear();
        grid.resetColors();
        State currState = stateList.getState(getLocation());
        for (State pState = goalState; pState != null
                && !pState.equals(currState); pState = pState.predecessor) {
            info("Find State: " + pState);
            path.add(pState);
            grid.putColor(pState.loc, PATH_COLOR);
        }
        path.add(currState);
        grid.putColor(currState.loc, PATH_COLOR);
        System.out.println(currState);
    }

    private class EdgeCost {
        private Map<String, Cost> costMap;

        public EdgeCost() {
            this.costMap = new HashMap<String, Cost>();
        }

        public Cost getCost(Location x, Location y) {
            String key = Tool.hash(x, y);
            if (costMap.containsKey(key)) {
                return costMap.get(key);
            }
            int deltaRow = Math.abs(x.getRow() - y.getRow());
            int deltaCol = Math.abs(x.getCol() - y.getCol());
            if (deltaRow + deltaCol == 1) {
                return new Cost("1");
            }
            if (deltaRow == 1 && deltaCol == 1) {
                return new Cost("1.4");
            }
            warn("Locations %s and %s are not adjacent.", x, y);
            return Cost.INFINITE;
        }

        public void setCost(Location x, Location y, Cost cost) {
            int deltaRow = Math.abs(x.getRow() - y.getRow());
            int deltaCol = Math.abs(x.getCol() - y.getCol());
            if (deltaRow + deltaCol != 1 && !(deltaRow == 1 && deltaCol == 1)) {
                warn("Locations %s and %s are not adjacent.", x, y);
                return;
            }
            String key = Tool.hash(x, y);
            costMap.put(key, cost);
            key = Tool.hash(y, x);
            costMap.put(key, cost);
        }
    }

    private class State implements Comparable<State> {

        private Location loc;
        private Cost g;
        private Cost rhs;
        private Cost h;
        private LpaStarKey k;
        private State predecessor;

        public State(Location loc) {
            this.loc = loc;
            this.g = Cost.INFINITE;
            this.rhs = Cost.INFINITE;
            this.h = Tool.euclideanDist(loc, goal);
            this.predecessor = null;
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
            return String.format("%s<-%s:{%s,%s,%s,%s} ", loc,
                    predecessor == null ? null : predecessor.loc, g, rhs, h, k);
        }
    }

    private class StateList {
        Map<Location, State> map;

        public StateList() {
            this.map = new HashMap<Location, State>();
        }

        public State getState(Location loc) {
            if (!map.containsKey(loc)) {
                map.put(loc, new State(loc));
            }
            return map.get(loc);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            List<State> states = new ArrayList<State>(map.values());
            Collections.sort(states, new Comparator<State>() {
                @Override
                public int compare(State s1, State s2) {
                    int deltaRow = s1.loc.getRow() - s2.loc.getRow();
                    int deltaCol = s1.loc.getCol() - s2.loc.getCol();
                    return deltaRow != 0 ? deltaRow : deltaCol;
                }
            });
            int row = states.get(0).loc.getRow();
            for (State s : states) {
                if (s.loc.getRow() != row) {
                    sb.append('\n');
                    row = s.loc.getRow();
                }
                sb.append(s);
            }
            sb.append('\n');
            return sb.toString();
        }
    }
}

class LpaStarKey implements Comparable<LpaStarKey> {
    private Cost k1;
    private Cost k2;

    public LpaStarKey(Cost k1, Cost k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public int compareTo(LpaStarKey k) {
        return compare(this, k);
    }

    public static int compare(LpaStarKey key1, LpaStarKey key2) {
        int result = Cost.compare(key1.k1, key2.k1);
        if (result != 0) {
            return result;
        }
        return Cost.compare(key1.k2, key2.k2);
    }

    @Override
    public String toString() {
        return String.format("(k1=%s,k2=%s)", k1, k2);
    }
}