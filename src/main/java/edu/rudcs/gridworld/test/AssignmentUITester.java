package edu.rudcs.gridworld.test;

import edu.rudcs.gridworld.agent.AdaptiveAstarAgent;
import edu.rudcs.gridworld.agent.Agent;
import edu.rudcs.gridworld.agent.RawAdaptiveAgent;
import edu.rudcs.gridworld.agent.RawRepeatBackwardAgent;
import edu.rudcs.gridworld.agent.RawRepeatForwardAgent;
import edu.rudcs.gridworld.agent.RepeatBackwardAstarAgent;
import edu.rudcs.gridworld.agent.RepeatForwardAstarAgent;
import edu.rudcs.gridworld.map.RepeatAstarSearchMap;

public class AssignmentUITester {
	
	public static final String map = "maps/map6.txt";
	
	public static void main(String[] args){
		AssignmentUITester at = new AssignmentUITester();
		RunRepeatAstarAgent.runRepeatAstarAgent("forward", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("backward", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("adaptive", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawforward", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawbackward", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawadaptive", map);
		//at.runRepeatAstarAgent();
	}

}

