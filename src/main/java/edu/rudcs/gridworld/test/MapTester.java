package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.map.Generator;
import edu.rudcs.gridworld.map.MapGenerator;

public class MapTester {

    public static void main(String[] args) {
        Generator gen = null;
        gen = new MapGenerator(101, 101);
        // gen = new MazeGenerator(50, 100, 0.5, 0.5);
<<<<<<< HEAD
        gen = new DfsMazeGenerator(100, 100);
        gen.generateMaps(1000);
=======
        // gen = new DfsMazeGenerator(25, 25);
        gen.generateMaps(1);
>>>>>>> ed0038e2aa103c6ff65b81dbedf26078f79d1b64
    }

}
