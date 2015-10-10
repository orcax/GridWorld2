package edu.rudcs.gridworld.agent;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import edu.rudcs.gridworld.map.actor.Goal;
import edu.rudcs.gridworld.map.actor.Path;
import edu.rudcs.gridworld.map.actor.Shadow;
import edu.rudcs.gridworld.map.actor.Wall;
import edu.rudcs.gridworld.util.TreeNode;

public class AStarAgent extends Agent {

    private class PathCost {
        public Location loc;
        public int cost;
        public int estimate;

        public PathCost(Location loc, int cost, int estimate) {
            this.loc = loc;
            this.cost = cost;
            this.estimate = estimate;
        }

        public boolean equals(PathCost pc) {
            return this.loc.equals(pc);
        }
    }

    private boolean start;
    private Location goal;
    private Queue<PathCost> frontiers;
    private Set<String> frontierSet;
    private Set<String> exploredSet;
    private TreeNode<PathCost> root;

    public AStarAgent(Location goal) {
        this.goal = goal;
        start = false;
        frontiers = new PriorityBlockingQueue<PathCost>(10,
                new Comparator<PathCost>() {
                    @Override
                    public int compare(PathCost pc1, PathCost pc2) {
                        return (pc1.cost + pc1.estimate)
                                - (pc2.cost + pc2.estimate);
                    }
                });
        frontierSet = new HashSet<String>();
        exploredSet = new HashSet<String>();
    }

    private int ManhattanDist(Location l1, Location l2) {
        return Math.abs(l1.getRow() - l2.getRow())
                + Math.abs(l1.getCol() - l2.getCol());
    }

    private PathCost getPathCost(Location loc) {
        PathCost tmp = new PathCost(loc, 0, 0);
        int cost = root.getDepth(tmp);
        int estimate = ManhattanDist(loc, goal);
        return new PathCost(loc, cost, estimate);
    }

    private boolean isNewLocation(Location loc) {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return false;
        }
        if (!grid.isValid(loc)) {
            return false;
        }
        String key = hash(loc);
        if (frontierSet.contains(key) || exploredSet.contains(key)) {
            return false;
        }
        Actor actor = grid.get(loc);
        return actor == null || !(actor instanceof Wall);
    }

    private List<Location> getValidAdjacentLocations() {
        System.out.println("get adjacent locations for " + getLocation());
        List<Location> locs = new ArrayList<Location>();
        for (int i = 0; i < Location.FULL_CIRCLE; i += Location.RIGHT) {
            Location loc = getLocation()
                    .getAdjacentLocation(getDirection() + i);
            if (isNewLocation(loc)) {
                locs.add(loc);
                System.out.print(loc + " ");
            }
        }
        System.out.println();
        return locs;
    }

    @Override
    public void moveTo(Location loc) {
        if (loc.equals(getLocation())) {
            return;
        }
        Location oldLoc = getLocation();
        int oldDir = getDirection();
        super.moveTo(loc);
        Grid<Actor> grid = getGrid();
        Shadow shadow = new Shadow(oldDir);
        shadow.putSelfInGrid(grid, oldLoc);
    }

    @Override
    public void act() {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        PathCost pc = null;
        boolean isGoal = false;
        if (!start) {
            start = true;
            Location loc = getLocation();
            pc = new PathCost(loc, 0, ManhattanDist(loc, goal));
            root = new TreeNode<PathCost>(pc);
            isGoal = grid.get(pc.loc) instanceof Goal;
        } else {
            if (frontiers.isEmpty()) {
                return;
            }
            pc = frontiers.poll();
            frontierSet.remove(hash(pc.loc));
            System.out.println("Move to " + pc.loc);
            isGoal = grid.get(pc.loc) instanceof Goal;
            if (!isGoal) {
                moveTo(pc.loc);
            }
        }
        TreeNode<PathCost> node = root.getChild(pc);
        if (isGoal) {
            // TODO
            Actor actor = grid.get(pc.loc);
            actor.setColor(Color.YELLOW);
            removeSelfFromGrid();
            while (node != null) {
                Path path = new Path();
                System.out.println(node.getData());
                path.putSelfInGrid(grid, node.getData().loc);
                System.out.println(path);
                node = node.getParent();
            }
            frontiers.clear();
            end = true;
            return;
        }
        exploredSet.add(hash(pc.loc));
        List<Location> locs = getValidAdjacentLocations();
        for (Location l : locs) {
            Actor actor = grid.get(l);
            PathCost childPc = getPathCost(l);
            frontiers.add(childPc);
            frontierSet.add(hash(l));
            node.addChild(new TreeNode<PathCost>(childPc));
        }
    }
    
}
