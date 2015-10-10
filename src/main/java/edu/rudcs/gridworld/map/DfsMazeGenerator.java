package edu.rudcs.gridworld.map;

import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DfsMazeGenerator extends Generator {

    public DfsMazeGenerator(int rows, int cols) {
        this.rows = rows / 2 * 2 + 1;
        this.cols = cols / 2 * 2 + 1;
        this.path = ROOT_PATH + "randmazes/";
    }

    @Override
    public char[][] generate() {
        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(map[i], WALL);
        }
        int row = new Random().nextInt(rows / 2) * 2 + 1;
        int col = new Random().nextInt(cols / 2) * 2 + 1;
        map[row][col] = ROAD;
        dfs(map, row, col);
        Location start = null, goal = null;
        for (int i = 1, j = 1; i < rows && j < cols;) {
            if (start != null && goal != null) {
                break;
            }
            if (start == null && map[i][j] == ROAD) {
                start = new Location(i, j);
                map[i][j] = START;
            } 
            if(goal == null && map[rows-i][cols-j] == ROAD) {
                goal = new Location(rows-i, cols-j);
                map[rows-i][cols-j] = GOAL;
            }
            else {
                if (i <= j)
                    ++i;
                else
                    ++j;
            }
        }
        return map;
    }

    private void dfs(char[][] map, int row, int col) {
        Location nextNeighbor = getNextNeighbor(map, row, col);
        while (nextNeighbor != null) {
            int nextRow = nextNeighbor.getRow();
            int nextCol = nextNeighbor.getCol();
            map[nextRow][nextCol] = ROAD;
            map[(row + nextRow) / 2][(col + nextCol) / 2] = ROAD;
            dfs(map, nextRow, nextCol);
            nextNeighbor = getNextNeighbor(map, row, col);
        }
    }

    private Location getNextNeighbor(char[][] map, int row, int col) {
        List<Location> neighbors = new ArrayList<Location>();
        // NORTH
        if (row - 2 > 0 && map[row - 2][col] != ROAD) {
            neighbors.add(new Location(row - 2, col));
        }
        // SOUTH
        if (row + 2 < rows && map[row + 2][col] != ROAD) {
            neighbors.add(new Location(row + 2, col));
        }
        // WEST
        if (col - 2 > 0 && map[row][col - 2] != ROAD) {
            neighbors.add(new Location(row, col - 2));
        }
        // EAST
        if (col + 2 < cols && map[row][col + 2] != ROAD) {
            neighbors.add(new Location(row, col + 2));
        }
        if (neighbors.size() == 0) {
            return null;
        }
        int n = new Random().nextInt(neighbors.size());
        return neighbors.get(n);
    }
}
