package edu.rudcs.gridworld.map;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeGenerator {

    private class Pair {
        int row;
        int col;

        public Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static final String MAZE_PATH = "maps/randmazes/";
    private static final String MAZE_SUFFIX = "txt";
    private static final char ROAD = ActorType.ROAD.toChar();
    private static final char WALL = ActorType.WALL.toChar();
    private static final char START = ActorType.START.toChar();
    private static final char GOAL = ActorType.GOAL.toChar();

    private int rows;
    private int cols;
    private int complexity;
    private int density;
    private Integer count;

    public MazeGenerator(int rows, int cols, double complexity, double density) {
        this.rows = rows / 2 * 2 + 1;
        this.cols = cols / 2 * 2 + 1;
        this.complexity = (int) (complexity * 5 * (rows + cols));
        this.density = (int) (density * (int) (rows / 2) * (int) (cols / 2));
        this.count = 0;
    }

    private char[][] generateByDfs() {
        char[][] map = new char[rows][cols];
        Arrays.fill(map[0], '1');
        Arrays.fill(map[rows - 1], '1');
        for (int i = 0; i < rows; i++) {
            Arrays.fill(map[i], '0');
            map[i][0] = '1';
            map[i][cols - 1] = '1';
        }
        for (int i = 0; i < density; i++) {
            int x = new Random().nextInt(cols / 2) * 2;
            int y = new Random().nextInt(rows / 2) * 2;
            map[y][x] = '1';
            for (int j = 0; j < complexity; j++) {
                List<Pair> neighbors = new ArrayList<Pair>();
                if (x > 1)
                    neighbors.add(new Pair(y, x - 2));
                if (x < cols - 2)
                    neighbors.add(new Pair(y, x + 2));
                if (y > 1)
                    neighbors.add(new Pair(y - 2, x));
                if (y < rows - 2)
                    neighbors.add(new Pair(y + 2, x));
                if (neighbors.size() == 0)
                    continue;
                Random seed = new Random();
                Pair next = neighbors
                        .get(new Random(seed.nextLong()).nextInt(neighbors.size()));
                int y0 = next.row, x0 = next.col;
                if (map[y0][x0] == '0') {
                    map[y0][x0] = '1';
                    map[y0 + (y - y0) / 2][x0 + (x - x0) / 2] = '1';
                    x = x0;
                    y = y0;
                }
            }
        }
        map[1][1] = 's';
        map[rows - 2][cols - 2] = 'g';
        return map;
    }
    
    private String getMapName() {
        return String.format("%d.%s", ++count, MAZE_SUFFIX);
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
            pw = new PrintWriter(MAZE_PATH + getMapName(), "utf-8");
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
    
    public void generateMaps(int n) {
        for (int i = 0; i < n; i++) {
            char[][] map = generateByDfs();
            saveAsFile(map);
        }
    }

}