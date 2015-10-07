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

public class SearchMapRunner {

    public static void main(String[] args) {
        SearchMapRunner runner = new SearchMapRunner();
        // runner.runBfs();
        //runner.runAStar();
        // runner.runDStar();
        // runner.runLpaStar();
        //runner.runDStarLiteAgent();
        runner.runRepeatAstarAgent();
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
        map.loadMap("maps/map4.txt");
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
    
    public void runRepeatAstarAgent(){
    	RepeatAstarSearchMap map = new RepeatAstarSearchMap();
    	 map.loadMap("maps/map4.txt");
        
         Agent agent = getRepeatAgent("adaptive",map);
         map.loadAgent(agent);
         map.loadWorld();
         map.show();
    }
    
    public Agent getRepeatAgent(String s, RepeatAstarSearchMap map){
    	switch(s){
    	
    	case "forward": return new RepeatForwardAstarAgent(map.getStarts().get(0),
    	        map.getGoals().get(0),map.getCells(),map.getRows(),map.getColumn());
    	
    	case "backward":return new RepeatBackwardAstarAgent(map.getStarts().get(0),
    	        map.getGoals().get(0),map.getCells(),map.getRows(),map.getColumn());
    	
    	case "adaptive":return new AdaptiveAstarAgent(map.getStarts().get(0),
    			map.getGoals().get(0),map.getCells(),map.getRows(),map.getColumn());
    	
    	}
    	return null;
    	
    }
}
