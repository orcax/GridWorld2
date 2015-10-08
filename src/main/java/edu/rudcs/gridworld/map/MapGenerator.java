package edu.rudcs.gridworld.map;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

public class MapGenerator {

    private static final String MAP_PATH = "maps/randmaps/";
    private static final String MAP_SUFFIX = "txt";
    private static final char ROAD = '0';
    private static final char WALL = '1';
    private static final char START = 's';
    private static final char GOAL = 'e';

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

    private void genRandom() {
        char[][] map = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(map[i], '0');
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
        saveAsFile(map);
    }

    public void generateMaps(int n) {
        for (int i = 0; i < n; i++) {
            genRandom();
        }
    }

    public static void main(String[] args) {
        MapGenerator mapGen = new MapGenerator();
        mapGen.generateMaps(100);
    }

}
