package edu.rudcs.gridworld.agent;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import edu.rudcs.gridworld.map.Goal;
import edu.rudcs.gridworld.map.Path;
import edu.rudcs.gridworld.map.Shadow;
import edu.rudcs.gridworld.map.Wall;
import edu.rudcs.gridworld.util.TreeNode;

public class BfsAgent extends Agent {

    private boolean start;
    private Queue<Location> frontiers;
    private Set<String> frontierSet;
    private Set<String> explored;
    private TreeNode<Location> root;

    public BfsAgent() {
        start = false;
        frontiers = new LinkedBlockingQueue<Location>();
        frontierSet = new HashSet<String>();
        explored = new HashSet<String>();
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
        if (frontierSet.contains(key) || explored.contains(key)) {
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
        Location loc = null;
        if (!start) {
            start = true;
            loc = getLocation();
            root = new TreeNode<Location>(loc);
            if (grid.get(loc) instanceof Goal) {
                return;
            }
        } else {
            if (frontiers.isEmpty()) {
                return;
            }
            loc = frontiers.poll();
            frontierSet.remove(hash(loc));
            System.out.println("Move to " + loc);
            moveTo(loc);
        }
        explored.add(hash(loc));
        TreeNode<Location> node = root.getChild(loc);
        List<Location> locs = getValidAdjacentLocations();
        for (Location l : locs) {
            Actor actor = grid.get(l);
            if (actor instanceof Goal) {
                actor.setColor(Color.YELLOW);
                removeSelfFromGrid();
                while (node != null) {
                    Path path = new Path();
                    System.out.println(node.getData());
                    path.putSelfInGrid(grid, node.getData());
                    System.out.println(path);
                    node = node.getParent();
                }
                frontiers.clear();
                return;
            }
            frontiers.add(l);
            frontierSet.add(hash(l));
            node.addChild(new TreeNode<Location>(l));
        }
    }

}
