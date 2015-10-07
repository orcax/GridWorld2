package edu.rudcs.gridworld.map;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

public class MapGenerator {

    private static final String MAP_PATH = "maps/";
    private static final String MAP_PREFIX = "randmap-";
    private static final String MAP_SUFFIX = ".txt";

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
        StringBuffer sb = new StringBuffer(MAP_PREFIX);
        for (int i = count.toString().length(); i < 4; i++) {
            sb.append("0");
        }
        sb.append(++count);
        sb.append(MAP_SUFFIX);
        return sb.toString();
    }

    private void generate() {
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

    public void generateMaps(int n) {
    }

    public static void main(String[] args) {
        MapGenerator mapGen = new MapGenerator();
        mapGen.generate();
        System.out.println();
    }

}
