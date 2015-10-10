package edu.rudcs.gridworld.map;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import edu.rudcs.gridworld.map.actor.ActorType;

public abstract class Generator {

    protected static final String ROOT_PATH = "maps/";
    protected static final String SUFFIX = "txt";
    protected static final char ROAD = ActorType.ROAD.toChar();
    protected static final char WALL = ActorType.WALL.toChar();
    protected static final char START = ActorType.START.toChar();
    protected static final char GOAL = ActorType.GOAL.toChar();

    protected int rows;
    protected int cols;
    protected int count = 0;
    protected String path = ROOT_PATH;

    protected String getMapName() {
        return String.format("%d.%s", ++count, SUFFIX);
    }

    protected void saveAsFile(char[][] map) {
        StringBuffer sb = new StringBuffer(rows + " " + cols + "\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(map[i][j] + " ");
            }
            sb.append("\n");
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(path + getMapName(), "utf-8");
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
            char[][] map = generate();
            saveAsFile(map);
        }
    }
    
    public abstract char[][] generate();
}
