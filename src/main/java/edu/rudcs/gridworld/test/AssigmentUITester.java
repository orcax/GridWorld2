package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.agent.RepeatBackwardAstarAgent;
import edu.rudcs.gridworld.agent.RepeatForwardAstarAgent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;

public class AssigmentUITester {
	
	public static void main(String[] args){
		AssigmentUITester at = new AssigmentUITester();
		at.runRepeatAstarAgent();
	}

    public void runRepeatAstarAgent() {
        RepeatAstarSearchMap map = new RepeatAstarSearchMap();
        map.loadMap("maps/randmazes/1.txt");
        Agent agent = getRepeatAgent("backward", map);
        map.loadAgent(agent);
        map.loadWorld();
        map.show();
    }

    public Agent getRepeatAgent(String s, RepeatAstarSearchMap map) {
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
        }
        return null;

    }
}
