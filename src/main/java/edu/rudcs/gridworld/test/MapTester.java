package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.map.MazeGenerator;

public class MapTester {

    public static void main(String[] args) {
        // MapGenerator mapGen = new MapGenerator();
        // mapGen.generateMaps(100);

        MazeGenerator mazeGen = new MazeGenerator(50, 100, 0.5, 0.5);
        mazeGen.generateMaps(100);
    }

}
