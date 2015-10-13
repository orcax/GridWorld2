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
    
	public int MAP_COUNT = 1000;
	public String MAP_NAME = "maps/randmaps/";
	
    public void testRepeatAstar(String name) {
    	int expand = 0; 
    	int explore = 0;
    	int avg_expand = 0;
    	int avg_explore = 0;
    	int totalcounter = 0;
    	int rate = 0;
        for (int i = 1; i <= MAP_COUNT; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap(MAP_NAME + i + ".txt");
            RepeatAstarAgent agent = getRepeatAgent(name,map);
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
            
            int counter = agent.getTotalCounter();
            totalcounter += counter;
            expand += agent.getTotalExpand();
            explore += agent.getTotalexplore();
            avg_expand += agent.getTotalExpand()/counter;
            avg_explore += agent.getTotalexplore()/counter;
            rate += agent.getRate();
        }
        RepeatAstarAgent.FinalRecord(expand/MAP_COUNT,explore/MAP_COUNT,avg_expand/MAP_COUNT, avg_explore/MAP_COUNT,totalcounter/MAP_COUNT, rate/MAP_COUNT, name);
    }

    /*
    public void testRepeatBackwardAstar() {
    	int expand = 0; 
    	int explore = 0;
    	int rate = 0;
    	int avg_expand = 0;
    	int totalcounter = 0;
    	int avg_explore = 0;
        for (int i = 1; i <= MAP_COUNT; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap(MAP_NAME + i + ".txt");
            RepeatBackwardAstarAgent agent = new RepeatBackwardAstarAgent(map.getStarts().get(0),
                    map.getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
            
            int counter = agent.getTotalCounter();
            totalcounter += counter;
            expand += agent.getTotalExpand();
            explore += agent.getTotalexplore();
            avg_expand += agent.getTotalExpand()/counter;
            avg_explore += agent.getTotalexplore()/counter;
            rate += agent.getRate();
        }
        
        RepeatBackwardAstarAgent.FinalRecord(expand/MAP_COUNT, explore/MAP_COUNT,avg_expand/MAP_COUNT, avg_explore/MAP_COUNT,totalcounter/MAP_COUNT, rate/MAP_COUNT, RepeatBackwardAstarAgent.class.getName());
        
    }

    public void testAdaptiveAstar() {
    	int expand = 0; 
    	int explore = 0;
    	int rate = 0;
    	int avg_expand = 0;
    	int avg_explore = 0;
    	int totalcounter = 0;
        for (int i = 1; i <= MAP_COUNT; i++) {
            RepeatAstarSearchMap map = new RepeatAstarSearchMap();
            map.loadMap(MAP_NAME + i + ".txt");
            AdaptiveAstarAgent agent = new AdaptiveAstarAgent(map.getStarts().get(0), map
                    .getGoals().get(0), map.getCells(), map.getRows(),
                    map.getColumn());
            map.loadAgent(agent);
            map.loadWorld();
            map.run();
            System.out.println(i);
            
            int counter = agent.getTotalCounter();
            totalcounter += counter;
            expand += agent.getTotalExpand();
            explore += agent.getTotalexplore();
            avg_expand += agent.getTotalExpand()/counter;
            avg_explore += agent.getTotalexplore()/counter;
            rate += agent.getRate();
            
        }

        AdaptiveAstarAgent.FinalRecord(expand/MAP_COUNT, explore/MAP_COUNT,avg_expand/MAP_COUNT, avg_explore/MAP_COUNT,totalcounter/MAP_COUNT, rate/MAP_COUNT, AdaptiveAstarAgent.class.getName());
    }*/
    
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
        at.testRepeatAstar("forward");
        at.testRepeatAstar("backward");
        at.testRepeatAstar("adaptive");
        at.testRepeatAstar("Rawforward");
        at.testRepeatAstar("Rawbackward");
        at.testRepeatAstar("Rawadaptive");
       // at.testRepeatBackwardAstar();
      //  at.testAdaptiveAstar();
    }

}
