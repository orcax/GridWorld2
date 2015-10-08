package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AStarAgent;
import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;
import edu.rudcs.gridworld.map.SearchMap;

public class SearchMapTester {

    public static void main(String[] args) {
    	
        RepeatAstarSearchMap map = new RepeatAstarSearchMap();
        for (int i = 1; i < 100; i++) {
            map.loadMap("maps/randmaps/" + i + ".txt");
            Agent agent = new AdaptiveAstarAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
        }
        
        
    }

}
