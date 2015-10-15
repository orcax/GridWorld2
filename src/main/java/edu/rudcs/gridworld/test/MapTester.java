package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.map.DfsMazeGenerator;
import edu.rudcs.gridworld.map.Generator;
import edu.rudcs.gridworld.map.MapGenerator;

public class MapTester {

    public static void main(String[] args) {
        Generator gen = null;
        gen = new MapGenerator(101, 101);
        gen.generateMaps(50);
        gen = new DfsMazeGenerator(101, 101);
        gen.generateMaps(50);
    }
}
