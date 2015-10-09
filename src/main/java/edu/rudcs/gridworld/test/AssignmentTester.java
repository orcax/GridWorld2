package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.agent.RepeatBackwardAstarAgent;
import edu.rudcs.gridworld.agent.RepeatForwardAstarAgent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;

public class AssignmentTester {
    
    public void testRepeatForwardAstar() {
        for (int i = 1; i <= 100; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap("maps/randmaps/" + i + ".txt");
            Agent agent = new RepeatForwardAstarAgent(map.getStarts().get(0),
                    map.getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
        }
    }

    public void testRepeatBackwardAstar() {
        for (int i = 1; i <= 100; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap("maps/randmaps/" + i + ".txt");
            Agent agent = new RepeatBackwardAstarAgent(map.getStarts().get(0),
                    map.getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
        }
    }

    public void testAdaptiveAstar() {
        for (int i = 1; i <= 100; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap("maps/randmaps/" + i + ".txt");
            Agent agent = new AdaptiveAstarAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
        }

    }

    public static void main(String[] args) {
        AssignmentTester at = new AssignmentTester();
        at.testRepeatForwardAstar();
        at.testRepeatBackwardAstar();
        at.testAdaptiveAstar();
    }

}
