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
	
	public static void main(String[] args){
		AssignmentUITester at = new AssignmentUITester();
		RunRepeatAstarAgent.runRepeatAstarAgent("forward", "maps/randmazes/1.txt");
		RunRepeatAstarAgent.runRepeatAstarAgent("backward", "maps/randmazes/1.txt");
		RunRepeatAstarAgent.runRepeatAstarAgent("adaptive", "maps/randmazes/1.txt");
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawforward", "maps/randmazes/1.txt");
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawbackward", "maps/randmazes/1.txt");
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawadaptive", "maps/randmazes/1.txt");
		//at.runRepeatAstarAgent();
	}

}

