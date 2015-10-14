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
	
	public static final String map1 = "maps/map7.txt";
	public static final String map2 = "maps/randmazes/1.txt";
	
	public static void main(String[] args){
		AssignmentUITester at = new AssignmentUITester();
		RunRepeatAstarAgent.runRepeatAstarAgent("forward", map1);
		//RunRepeatAstarAgent.runRepeatAstarAgent("backward", map1);
		RunRepeatAstarAgent.runRepeatAstarAgent("adaptive", map1);
		//RunRepeatAstarAgent.runRepeatAstarAgent("Rawforward", map1);
		//RunRepeatAstarAgent.runRepeatAstarAgent("Rawbackward", map1);
		//RunRepeatAstarAgent.runRepeatAstarAgent("Rawadaptive", map1);
		/*
		RunRepeatAstarAgent.runRepeatAstarAgent("forward", map2);
		RunRepeatAstarAgent.runRepeatAstarAgent("backward", map2);
		RunRepeatAstarAgent.runRepeatAstarAgent("adaptive", map2);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawforward", map2);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawbackward", map2);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawadaptive", map2);*/
		//at.runRepeatAstarAgent();
	}

}

