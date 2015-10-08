package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AStarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.map.SearchMap;

public class SearchMapTester {

    public static void main(String[] args) {
        SearchMap map = new SearchMap();
        for (int i = 1; i < 5; i++) {
            map.loadMap("maps/randmaps/" + i + ".txt");
            Agent agent = new AStarAgent(map.getGoal().get(0));
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
        }
    }

}
