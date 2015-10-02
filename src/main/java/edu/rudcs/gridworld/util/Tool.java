package edu.rudcs.gridworld.util;

import info.gridworld.grid.Location;

public class Tool {

    private Tool() {
    }

    public static String hash(Location loc) {
        return loc.getRow() + "," + loc.getCol();
    }

    public static String hash(Location x, Location y) {
        return String.format("%d,%d,%d,%d", x.getRow(), x.getCol(), y.getRow(),
                y.getCol());
    }

    public static Cost manhattanDist(Location l1, Location l2) {
        int dist = Math.abs(l1.getRow() - l2.getRow())
                + Math.abs(l1.getCol() - l2.getCol());
        return new Cost(dist);
    }

    public static Cost euclideanDist(Location l1, Location l2) {
        int d1 = l1.getRow() - l2.getRow();
        int d2 = l1.getCol() - l2.getCol();
        int d3 = d1 * d1 + d2 * d2;
        String d4 = String.format("%.1f", Math.sqrt(d3));
        return new Cost(d4);
    }

}
