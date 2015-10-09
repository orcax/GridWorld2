package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.map.MapGenerator;
import edu.rudcs.gridworld.map.MazeGenerator;

public class MapTester {

    public static void main(String[] args) {
//        MapGenerator mapGen = new MapGenerator();
//        mapGen.generateMaps(100);
        
        MazeGenerator mazeGen = new MazeGenerator(51, 101, 1, 1);
        mazeGen.generateMaps(10);
    }

}
