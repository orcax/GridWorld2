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
import java.util.Set;
import java.util.TreeSet;

import edu.rudcs.gridworld.map.actor.Goal;
import edu.rudcs.gridworld.map.actor.Wall;
import edu.rudcs.gridworld.util.Cost;
import edu.rudcs.gridworld.util.Tool;

public class DStarAgent extends Agent {

    private StateMap stateMap;
    private OpenList openList;
    private ArcCost arcCost;

    private Location start;
    private Location goal;
    private boolean isStarted;
    private boolean isPlanned;
    private boolean isStuck;
    private boolean isEnded;

    public DStarAgent(Location goal) {
        this.stateMap = new StateMap();
        this.openList = new OpenList(stateMap);
        this.arcCost = new ArcCost();

        this.goal = goal;
        this.isStarted = false;
        this.isPlanned = false;
        this.isStuck = false;
        this.isEnded = false;
    }

    @Override
    public void act() {
        if (isEnded) {
            return;
        }
        if (!isStarted) {
            start = getLocation();
            openList.insert(goal, new Cost("0"));
            isStarted = true;
        }

        try {
            if (!isPlanned) {
                info("FIRST PLANNING");
                Cost minK = processState();
                while (!minK.eq(new Cost("-1"))
                        && stateMap.getState(start).getT() != StateTag.CLOSED) {
                    minK = processState();
                }
                setPath();
                isPlanned = true;
            } else {
                State currState = stateMap.getState(getLocation());
                Location nextLoc = currState.getBackPointer();
                if (!isStuck) {
                    info("MOVING IN ACTION");
                    if (nextLoc == null) {
                        warn("FAILED TO FIND PATH! --END--");
                        isEnded = true;
                        return;
                    }
                    Grid<Actor> grid = getGrid();
                    if (grid == null) {
                        warn("GRID IS NULL!");
                        return;
                    }
                    Actor actor = grid.get(nextLoc);
                    if (actor instanceof Goal) {
                        info("REACH GOAL! --END--");
                        moveTo(nextLoc);
                        isEnded = true;
                        return;
                    } else if (actor instanceof Wall) {
                        info("DISCOVER WALL AT " + nextLoc + "!");
                        modifyCost(nextLoc, Cost.INFINITE);
                        isStuck = true;
                        return;
                    } else {
                        info("MOVE FROM " + getLocation() + " TO " + nextLoc);
                        moveTo(nextLoc);
                    }
                } else {
                    info("REPLANNING");
                    Cost minK = processState();
                    while (!minK.eq(new Cost("-1"))
                            && minK.lt(currState.getH())) {
                        info("k_min=" + minK + ", state=" + currState);
                        minK = processState();
                    }
                    setPath();
                    isStuck = false;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setPath() {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        grid.resetColors();

        State state = stateMap.getState(getLocation());
        while (state != null) {
            grid.putColor(state.getLoc(), PATH_COLOR);
            if (state.getBackPointer() == null) {
                state = null;
            } else {
                state = stateMap.getState(state.getBackPointer());
            }
        }
    }

    private Cost modifyCost(Location x, Cost cxy) throws Exception {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return new Cost("-1");
        }
        printf("Insert into open list: " + x);
        openList.insert(x);
        List<Location> neighbors = grid.getValidAdjacentLocations(x);
        for (Location y : neighbors) {
            arcCost.setCost(x, y, cxy);
            State state = stateMap.getState(y);
            if (state.getT() == StateTag.CLOSED) {
                printf("Insert into open list: " + state);
                openList.insert(state.getLoc());
            }
        }
        return openList.getMinK();
    }

    private Cost processState() throws Exception {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return new Cost("-1");
        }

        info(stateMap);
        State x = openList.getMinState();
        if (x == null) {
            return new Cost("-1");
        }
        info("#FUNCTION# Processing State " + x);
        Cost oldK = openList.getMinK();
        openList.delete(x.getLoc());
        List<Location> neighbors = grid.getValidAdjacentLocations(x.getLoc());
        if (oldK.lt(x.getH())) {
            info("#CONDITION# k_old < h(X):");
            for (Location l : neighbors) {
                State y = stateMap.getState(l);
                Cost cxy = arcCost.calc(x.getLoc(), y.getLoc());
                if (y.getH().leq(oldK) && x.getH().gt(y.getH().add(cxy))) {
                    info("#CONDITION# h(Y)<=k_old && h(X)>h(Y)+c(Y,X): Y=" + y);
                    x.setBackPointer(y.getLoc());
                    x.setH(y.getH().add(cxy));
                    info("UPDATE: " + x);
                }
            }
        }
        if (oldK.eq(x.getH())) {
            info("#CONDITION# k_old = h(X)");
            for (Location l : neighbors) {
                State y = stateMap.getState(l);
                Cost cxy = arcCost.calc(x.getLoc(), y.getLoc());
                Cost hxAndCxy = x.getH().add(cxy);
                if (y.getT() == StateTag.NEW
                        || (x.getLoc().equals(y.getBackPointer()) && !y.getH()
                                .eq(hxAndCxy))
                        || (!x.getLoc().equals(y.getBackPointer()) && y.getH()
                                .gt(hxAndCxy))) {
                    info("#CONDITION# t(Y)=NEW || (b(Y)=X && h(Y)!=h(X)+c(X,Y)) || (b(Y)!=X && h(Y)>h(X)+c(X,Y)): Y="
                            + y);
                    y.setBackPointer(x.getLoc());
                    openList.insert(y.getLoc(), hxAndCxy);
                    info("INSERT AND UPDATE: " + y);
                }
            }
        } else {
            info("#CONDITION# else");
            for (Location l : neighbors) {
                State y = stateMap.getState(l);
                Cost cxy = arcCost.calc(x.getLoc(), y.getLoc());
                Cost hxAndCxy = x.getH().add(cxy);
                Cost cyx = arcCost.calc(y.getLoc(), x.getLoc());
                Cost hyAndCyx = y.getH().add(cyx);
                if (y.getT() == StateTag.NEW
                        || (x.getLoc().equals(y.getBackPointer()) && !y.getH()
                                .eq(hxAndCxy))) {
                    info("#CONDITION# t(Y)=NEW || (b(Y)=X && h(Y)!=h(X)+c(X,Y)): Y="
                            + y);
                    y.setBackPointer(x.getLoc());
                    openList.insert(y.getLoc(), hxAndCxy);
                    info("INSERT AND UPDATE: " + y);
                } else {
                    if (!x.getLoc().equals(y.getBackPointer())
                            && y.getH().gt(hxAndCxy)) {
                        info("#CONDITION# b(Y)!=X && h(Y)>h(X)+c(X,Y): Y=" + y);
                        openList.insert(x.getLoc(), x.getH());
                        info("INSERT AND UPDATE: " + x);
                    } else if (!x.getLoc().equals(y.getBackPointer())
                            && x.getH().gt(hyAndCyx)
                            && y.getT() == StateTag.CLOSED && y.getH().gt(oldK)) {
                        info("#CONDITION# b(Y)!=X && h(X)>h(Y)+c(Y,X) && t(Y)=CLOSED && h(Y)>k_old: Y="
                                + y);
                        openList.insert(y.getLoc(), y.getH());
                        info("INSERT AND UPDATE: " + y);
                    }
                }
            }
        }
        return openList.getMinK();
    }

    private enum StateTag {
        NEW, OPEN, CLOSED
    }

    private class ArcCost {

        private Map<String, Cost> costMap;

        public ArcCost() {
            this.costMap = new HashMap<String, Cost>();
        }

        public Cost calc(Location x, Location y) throws Exception {
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
            System.out.println("Locations are not adjacent: " + x + " & " + y);
            return Cost.INFINITE;
        }

        public void setCost(Location x, Location y, Cost cost) throws Exception {
            int deltaRow = Math.abs(x.getRow() - y.getRow());
            int deltaCol = Math.abs(x.getCol() - y.getCol());
            if (deltaRow + deltaCol != 1 && !(deltaRow == 1 && deltaCol == 1)) {
                throw new Exception("Locations are not adjacent.");
            }
            String key = Tool.hash(x, y);
            costMap.put(key, cost);
            key = Tool.hash(y, x);
            costMap.put(key, cost);
        }
    }

    private class State {
        private Location loc;
        private Cost h;
        private Cost k;
        private StateTag t;
        private Location backPointer;

        public State(Location loc) {
            this.loc = loc;
            this.h = new Cost("-1");
            this.k = new Cost("-1");
            this.t = StateTag.NEW;
            this.backPointer = null;
        }

        public Location getLoc() {
            return loc;
        }

        public Cost getH() {
            return h;
        }

        public void setH(Cost h) {
            this.h = h;
        }

        public Cost getK() {
            return k;
        }

        public void setK(Cost k) {
            this.k = k;
        }

        public StateTag getT() {
            return t;
        }

        public void setT(StateTag t) {
            this.t = t;
        }

        public Location getBackPointer() {
            return backPointer;
        }

        public void setBackPointer(Location backPointer) {
            this.backPointer = backPointer;
        }

        public int hashCode() {
            return this.loc.hashCode();
        }

        @Override
        public String toString() {
            return String.format("%s->%s, h=%s, k=%s, t=%s", loc, backPointer,
                    h, k, t);
        }
    }

    private class StateMap {

        private Map<Location, State> stateMap;

        public StateMap() {
            stateMap = new HashMap<Location, State>();
        }

        public State getState(Location loc) {
            if (!stateMap.containsKey(loc)) {
                State state = new State(loc);
                stateMap.put(loc, state);
            }
            return stateMap.get(loc);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("STATES TABLE\n");
            List<State> states = new ArrayList<State>(stateMap.values());
            Collections.sort(states, new Comparator<State>() {
                @Override
                public int compare(State s1, State s2) {
                    int deltaRow = s1.getLoc().getRow() - s2.getLoc().getRow();
                    int deltaCol = s1.getLoc().getCol() - s2.getLoc().getCol();
                    return deltaRow != 0 ? deltaRow : deltaCol;
                }
            });
            int row = states.get(0).getLoc().getRow();
            for (State s : states) {
                if (s.getLoc().getRow() != row) {
                    sb.append('\n');
                    row = s.getLoc().getRow();
                }
                sb.append(String.format("[%d,%d]:(%s,%s) ",
                        s.getLoc().getRow(), s.getLoc().getCol(), s.getH(),
                        s.getK()));
            }
            sb.append('\n');
            return sb.toString();
        }
    }

    private class OpenList {
        private StateMap stateMap;
        private Set<Location> openSet;

        public OpenList(StateMap stateMap) {
            this.stateMap = stateMap;
            this.openSet = new TreeSet<Location>();
        }

        /**
         * Return the state on the OPEN list with minimum k.
         */
        public State getMinState() {
            State minState = null;
            for (Location l : openSet) {
                State s = stateMap.getState(l);
                if (minState == null || minState.getK().gt(s.getK())) {
                    minState = s;
                }
            }
            return minState;
        }

        /**
         * Return k_min for the OPEN list.
         */
        public Cost getMinK() {
            State minState = getMinState();
            return minState != null ? minState.getK() : new Cost("-1");
        }

        /**
         * Delete state X from the OPEN list.
         */
        public void delete(Location loc) {
            openSet.remove(loc);
            State state = stateMap.getState(loc);
            state.setT(StateTag.CLOSED);
        }

        /**
         * Compute k(X) and place or reposition state X on the OPEN list.
         */
        public void insert(Location loc, Cost h) {
            State state = stateMap.getState(loc);
            if (state.getT() == StateTag.NEW) {
                state.setK(h);
            } else if (state.getT() == StateTag.OPEN) {
                state.setK(Cost.min(state.getK(), h));
            } else if (state.getT() == StateTag.CLOSED) {
                state.setK(Cost.min(state.getH(), h));
            }
            state.setH(h);
            state.setT(StateTag.OPEN);
            openSet.add(loc);
        }

        /**
         * Place or reposition state X on the OPEN list.
         */
        public void insert(Location loc) {
            State state = stateMap.getState(loc);
            state.setT(StateTag.OPEN);
            if (!openSet.contains(loc)) {
                openSet.add(loc);
            }
        }
    }
}
