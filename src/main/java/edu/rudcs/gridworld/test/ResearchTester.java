package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AStarAgent;
import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.agent.BfsAgent;
import edu.rudcs.gridworld.agent.DStarAgent;
import edu.rudcs.gridworld.agent.DStarLiteAgent;
import edu.rudcs.gridworld.agent.LpaStarAgent;
import edu.rudcs.gridworld.agent.RepeatAstarAgent;
import edu.rudcs.gridworld.agent.RepeatBackwardAstarAgent;
import edu.rudcs.gridworld.agent.RepeatForwardAstarAgent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;
import edu.rudcs.gridworld.map.SearchMap;

public class ResearchTester {

    public static void main(String[] args) {
        ResearchTester runner = new ResearchTester();
        // runner.runBfs();
        // runner.runAStar();
        // runner.runDStar();
        // runner.runLpaStar();
        // runner.runDStarLiteAgent();
        //runner.runRepeatAstarAgent();
    }

    public void runBfs() {
        SearchMap map = new SearchMap();
        map.loadMap("maps/map1.txt");
        Agent agent = new BfsAgent();
        map.loadAgent(agent);
        map.loadWorld();
        map.show();
    }

    public void runAStar() {
        SearchMap map = new SearchMap();
        map.loadMap("maps/map4.txt");
        Agent agent = new AStarAgent(map.getGoal().get(0));
        map.loadAgent(agent);
        map.loadWorld();
        map.show();
    }

    public void runDStar() {
        SearchMap map = new SearchMap();
        map.loadMap("maps/randmaps/1.txt");
        Agent agent = new DStarAgent(map.getGoal().get(0));
        map.loadAgent(agent);
        map.loadWorld();
        map.show();
    }

    public void runLpaStar() {
        SearchMap map = new SearchMap();
        map.loadMap("maps/map3.txt");
        Agent agent = new LpaStarAgent(map.getGoal().get(0));
        map.loadAgent(agent);
        map.loadWorld();
        map.show();
    }

    public void runDStarLiteAgent() {
        SearchMap map = new SearchMap();
        map.loadMap("maps/map4.txt");
        Agent agent = new DStarLiteAgent(map.getGoal().get(0));
        map.loadAgent(agent);
        map.loadWorld();
        map.show();
    }


}
