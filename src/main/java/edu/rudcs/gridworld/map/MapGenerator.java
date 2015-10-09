package edu.rudcs.gridworld.map;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class MapGenerator {

    private class Pair {
        int row;
        int col;

        public Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static final String MAP_PATH = "maps/randmaps/";
    private static final String MAP_SUFFIX = "txt";
    private static final char ROAD = ActorType.ROAD.toChar();
    private static final char WALL = ActorType.WALL.toChar();
    private static final char START = ActorType.START.toChar();
    private static final char GOAL = ActorType.GOAL.toChar();
    private static final char UNVISITED = '\0';

    private int cols;
    private int rows;
    private int nWalls;
    private Integer count;

    public MapGenerator() {
        this.cols = 101;
        this.rows = 101;
        this.nWalls = (int) (0.3 * cols * rows);
        this.count = 0;
    }

    public MapGenerator(int cols, int rows, int nWalls) {
        this.cols = cols;
        this.rows = rows;
        this.nWalls = nWalls;
        this.count = 0;
    }

    private String getMapName() {
        return String.format("%d.%s", ++count, MAP_SUFFIX);
    }

    private void saveAsFile(char[][] map) {
        StringBuffer sb = new StringBuffer(rows + " " + cols + "\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(map[i][j] + " ");
            }
            sb.append("\n");
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(MAP_PATH + getMapName(), "utf-8");
            pw.write(sb.toString());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private int hash(int row, int col) {
        return row * cols + col;
    }

    private int hash(Pair pair) {
        return hash(pair.row, pair.col);
    }

    private Pair dehash(int hashVal) {
        int col = hashVal % cols;
        int row = hashVal / cols;
        return new Pair(row, col);
    }

    private Pair[] generateStartEnd() {
        Random rand = new Random();
        return new Pair[] { new Pair(rand.nextInt(rows), rand.nextInt(cols)),
                new Pair(rand.nextInt(rows), rand.nextInt(cols)), };
    }

    private char[][] generateByRandom() {
        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(map[i], ROAD);
        }
        Random rowRand = new Random();
        Random colRand = new Random();
        for (int i = 0; i < nWalls; i++) {
            int row = rowRand.nextInt(rows);
            int col = colRand.nextInt(cols);
            if (map[row][col] == '0') {
                map[row][col] = '1';
            } else {
                i--;
            }
        }
        map[rowRand.nextInt(rows)][colRand.nextInt(cols)] = 's';
        map[rowRand.nextInt(rows)][colRand.nextInt(cols)] = 'e';
        return map;
    }

    private char[][] generateByDfs() {
        char[][] map = new char[rows][cols];
        Set<Integer> unvisited = new TreeSet<Integer>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                unvisited.add(hash(i, j));
            }
            Arrays.fill(map[i], UNVISITED);
        }
        while (!unvisited.isEmpty()) {
            int randNo = new Random().nextInt(unvisited.size());
            Integer hashRoot = new ArrayList<Integer>(unvisited).get(randNo);
            Stack<Integer> stack = new Stack<Integer>();
            stack.add(hashRoot);
            while (!stack.isEmpty()) {
                Integer hashPos = stack.pop();
                Pair pos = dehash(hashPos);
                if (map[pos.row][pos.col] == UNVISITED) {
                    int type = new Random().nextInt(10);
                    if (type < 3) {
                        map[pos.row][pos.col] = WALL;
                    } else {
                        map[pos.row][pos.col] = ROAD;
                    }
                    unvisited.remove(hashPos);
                }
                List<Integer> neighbors = new ArrayList<Integer>();
                // NORTH
                if (pos.row - 1 >= 0 && map[pos.row - 1][pos.col] == UNVISITED) {
                    neighbors.add(hash(new Pair(pos.row - 1, pos.col)));
                }
                // SOUTH
                if (pos.row + 1 < rows && map[pos.row + 1][pos.col] == '\0') {
                    neighbors.add(hash(new Pair(pos.row + 1, pos.col)));
                }
                // WEST
                if (pos.col - 1 >= 0 && map[pos.row][pos.col - 1] == '\0') {
                    neighbors.add(hash(new Pair(pos.row, pos.col - 1)));
                }
                // EAST
                if (pos.col + 1 < cols && map[pos.row][pos.col + 1] == '\0') {
                    neighbors.add(hash(new Pair(pos.row, pos.col + 1)));
                }
                if (neighbors.size() == 0) {
                    continue;
                }
                int next = new Random().nextInt(neighbors.size());
                stack.add(neighbors.get(next));
            }
        }
        Pair[] points = generateStartEnd();
        map[points[0].row][points[0].col] = START;
        map[points[1].row][points[1].col] = GOAL;
        return map;
    }

    public void generateMaps(int n) {
        for (int i = 0; i < n; i++) {
            char[][] map = generateByDfs();
            saveAsFile(map);
        }
    }

}
