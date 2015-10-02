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
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.PriorityBlockingQueue;

import edu.rudcs.gridworld.map.Wall;
import edu.rudcs.gridworld.util.Cost;
import edu.rudcs.gridworld.util.Tool;

public class DStarLiteAgent extends Agent {

    private StateList stateList;
    private Queue<State> queueU;

    private Location start;
    private Location goal;
    private Cost km;
    // private State lastState;
    private boolean isStart;
    private boolean isEnd;

    public DStarLiteAgent(Location goal) {
        this.stateList = new StateList();
        this.queueU = new PriorityBlockingQueue<State>();

        this.goal = goal;
        // this.lastState = null;
        this.isStart = false;
        this.isEnd = false;
    }

    @Override
    public void act() {
        if (!isStart) {
            isStart = true;
            start = getLocation();
            initialize();
            computeShortestPath();
            tracePath();
            return;
        }

        if (isEnd || start.equals(goal)) {
            isEnd = true;
            return;
        }

        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        State nextState = stateList.getState(start).next;
        Actor actor = grid.get(nextState.loc);
        if (actor instanceof Wall) {
            km = km.add(getHeuristic(start, nextState.loc));
            nextState.occupied = true;
            // Just want to get neighbors by using getSuccs
            List<State> neighbors = getSuccs(nextState);
            for (State n : neighbors) {
                updateVertex(n);
            }
            computeShortestPath();
            tracePath();
        } else {
            moveTo(nextState.loc);
            // lastState = stateList.getState(start);
            start = nextState.loc;
        }
    }

    private void tracePath() {
        info("#FUNCTION# TracePath");
        // TODO how to check infinite loop?
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        grid.resetColors();
        State currState = stateList.getState(getLocation());
        Set<State> visited = new TreeSet<State>();
        info(stateList);
        while (!currState.loc.equals(goal)) {
            info("%s", currState);
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
        State curr = stateList.getState(start);
        while (curr.next != null) {
            sb.append(String.format("%s-[%s]->", curr.loc,
                    getCost(curr, curr.next).add(curr.next.g)));
            curr = curr.next;
        }
        sb.append(String.format("%s:GOAL", curr.loc));
        info(sb);
    }

    public DStarLiteKey calculateKey(State s) {
        // {01}
        Cost k2 = Cost.min(s.g, s.rhs);
        Cost k1 = k2.add(s.h).add(km);
        return new DStarLiteKey(k1, k2);
    }

    public void initialize() {
        info("#FUNC START# initialize()");
        // {02}
        queueU.clear();
        // {03}
        km = new Cost("0.0");
        // {05}
        State goalState = stateList.getState(goal);
        goalState.rhs = new Cost(0);
        // {06}
        goalState.k = calculateKey(goalState);
        queueU.add(goalState);
        info("#FUNC END# initialize()");
    }

    public void updateVertex(State state) {
        info("#FUNC START# updateVertex(%s)", state);
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
        info("#FUNC END# updateVertex(%s)", state);
    }

    public void computeShortestPath() {
        info("#FUNC START# computeShortestPath()");
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }

        // {10}
        State top = queueU.peek();
        State startState = stateList.getState(start);
        while (top != null && top.k.compareTo(calculateKey(startState)) < 0
                || startState.rhs.neq(startState.g)) {
            // {11}
            DStarLiteKey kOld = top.k;
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
        info("#FUNC END# computeShortestPath()");
    }

    private List<State> getPreds(Location loc) {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return null;
        }
        List<State> preds = new ArrayList<State>();
        List<Location> neighbors = grid.getValidAdjacentLocations(loc);
        for (Location n : neighbors) {
            State s = stateList.getState(n);
            if (!s.occupied) {
                preds.add(s);
            }
        }
        return preds;
    }

    private List<State> getPreds(State state) {
        return getPreds(state.loc);
    }

    private List<State> getSuccs(Location loc) {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return null;
        }
        List<State> succs = new ArrayList<State>();
        List<Location> neighbors = grid.getValidAdjacentLocations(loc);
        for (Location n : neighbors) {
            State s = stateList.getState(n);
            if (!s.occupied) {
                succs.add(s);
            }
        }
        return succs;
    }

    private List<State> getSuccs(State state) {
        return getSuccs(state.loc);
    }

    private Cost getCost(State s1, State s2) {
        if (!s1.occupied && !s2.occupied) {
            return getHeuristic(s1, s2);
        }
        return Cost.INFINITE;
    }

    private Cost getCost(Location l1, Location l2) {
        State s1 = stateList.getState(l1);
        State s2 = stateList.getState(l2);
        return getCost(s1, s2);
    }

    private Cost getHeuristic(Location l1, Location l2) {
        return Tool.euclideanDist(l1, l2);
    }

    private Cost getHeuristic(State s1, State s2) {
        return Tool.euclideanDist(s1.loc, s2.loc);
    }

    private class State implements Comparable<State> {

        private Location loc;
        private Cost g;
        private Cost rhs;
        private Cost h;
        private DStarLiteKey k;
        private State next;
        private boolean occupied;

        public State(Location loc) {
            this.loc = loc;
            this.g = Cost.INFINITE;
            this.rhs = Cost.INFINITE;
            this.h = Tool.euclideanDist(loc, start);
            this.next = null;
            this.occupied = false;
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
            return String.format("%s:{%s,%s,%s,%s} ", loc, g, rhs, h, occupied);
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

class DStarLiteKey implements Comparable<DStarLiteKey> {
    private Cost k1;
    private Cost k2;

    public DStarLiteKey(Cost k1, Cost k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public int compareTo(DStarLiteKey k) {
        return compare(this, k);
    }

    public static int compare(DStarLiteKey key1, DStarLiteKey key2) {
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
