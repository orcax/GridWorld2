package edu.rudcs.gridworld.test;


public class AssignmentUITester {
	
	public static final String map1 = "maps/map1.txt";
	public static final String map2 = "maps/randmazes/1.txt";
	public static final String map3 = "maps/randmaps/1.txt";
	
	public static void main(String[] args){
		run(map1);
		run(map2);
		run(map3);
	}
	
	private static void run(String map){
		RunRepeatAstarAgent.runRepeatAstarAgent("adaptive", map);
	    RunRepeatAstarAgent.runRepeatAstarAgent("backward", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("forward", map);
		RunRepeatAstarAgent.runRepeatAstarAgent("Rawforward", map);
	}

}

