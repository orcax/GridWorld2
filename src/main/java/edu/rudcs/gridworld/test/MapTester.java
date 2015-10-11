package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.map.DfsMazeGenerator;
import edu.rudcs.gridworld.map.Generator;

public class MapTester {

    public static void main(String[] args) {
        Generator gen = null;
        // gen = new MapGenerator();
        // gen = new MazeGenerator(50, 100, 0.5, 0.5);
        gen = new DfsMazeGenerator(100, 100);
        gen.generateMaps(1000);
    }

}
