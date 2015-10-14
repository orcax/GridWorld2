package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.agent.RawAdaptiveAgent;
import edu.rudcs.gridworld.agent.RawRepeatBackwardAgent;
import edu.rudcs.gridworld.agent.RawRepeatForwardAgent;
import edu.rudcs.gridworld.agent.RepeatAstarAgent;
import edu.rudcs.gridworld.agent.RepeatBackwardAstarAgent;
import edu.rudcs.gridworld.agent.RepeatForwardAstarAgent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;

public class AssignmentTester {
    
	public int MAP_COUNT = 100;
	public  static String MAP_MAZE = "maps/randmazes/";
	public  static String MAP_RAND = "maps/randmaps/";
	
    public void testRepeatAstar(String name,String mapName) {
    	int expand = 0; 
    	int explore = 0;
    	int avg_expand = 0;
    	int avg_explore = 0;
    	int totalcounter = 0;
    	int rate = 0;
    	int succ = 0;
        for (int i = 1; i <= MAP_COUNT; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap(mapName + i + ".txt");
            RepeatAstarAgent agent = getRepeatAgent(name,map);
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
            
            if(agent.getStatus()){
            	succ++;
                int counter = agent.getTotalCounter();
                totalcounter += counter;
                expand += agent.getTotalExpand();
                explore += agent.getTotalexplore();
                avg_expand += agent.getTotalExpand()/counter;
                avg_explore += agent.getTotalexplore()/counter;
                rate += agent.getRate();
            }
        }
        RepeatAstarAgent.FinalRecord(expand/succ,explore/succ,avg_expand/succ, avg_explore/succ,totalcounter/succ, rate/succ, name);
    }

    
    public  RepeatAstarAgent getRepeatAgent(String s, RepeatAstarSearchMap map) {
        switch (s) {
        case "forward":
            return new RepeatForwardAstarAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
        case "backward":
            return new RepeatBackwardAstarAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
        case "adaptive":
            return new AdaptiveAstarAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
        case "Rawforward":
            return new RawRepeatForwardAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
        case "Rawbackward":
            return new RawRepeatBackwardAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
        case "Rawadaptive":
            return new RawAdaptiveAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
        }
        return null;

    }

    public static void main(String[] args) {
        AssignmentTester at = new AssignmentTester();
        at.testRepeatAstar("forward",MAP_MAZE);
        at.testRepeatAstar("backward",MAP_MAZE);
        at.testRepeatAstar("adaptive",MAP_MAZE);
        at.testRepeatAstar("Rawforward",MAP_MAZE);
        
        at.testRepeatAstar("forward",MAP_RAND);
        at.testRepeatAstar("backward",MAP_RAND);
        at.testRepeatAstar("adaptive",MAP_RAND);
        at.testRepeatAstar("Rawforward",MAP_RAND);
        //at.testRepeatAstar("Rawbackward");
        //at.testRepeatAstar("Rawadaptive");
       // at.testRepeatBackwardAstar();
      //  at.testAdaptiveAstar();
    }

}
