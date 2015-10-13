package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.agent.RawAdaptiveAgent;
import edu.rudcs.gridworld.agent.RawRepeatBackwardAgent;
import edu.rudcs.gridworld.agent.RawRepeatForwardAgent;
import edu.rudcs.gridworld.agent.RepeatBackwardAstarAgent;
import edu.rudcs.gridworld.agent.RepeatForwardAstarAgent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;

public class RunRepeatAstarAgent {
	  public static void runRepeatAstarAgent(String name,String mapName) {
	        RepeatAstarSearchMap map = new RepeatAstarSearchMap();


	        map.loadMap(mapName);

	        Agent agent = getRepeatAgent(name, map);

	        map.loadAgent(agent);
	        map.loadWorld();
	        map.show();
	    }

	    private static Agent getRepeatAgent(String s, RepeatAstarSearchMap map) {
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

}
