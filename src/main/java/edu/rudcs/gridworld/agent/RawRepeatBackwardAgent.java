package edu.rudcs.gridworld.agent;

import java.util.Comparator;

import edu.rudcs.gridworld.util.BinaryHeap;
import edu.rudcs.gridworld.util.State;

public class RawRepeatBackwardAgent extends RepeatBackwardAstarAgent {

	public RawRepeatBackwardAgent(State start, State goal, byte[][] cells, int rows, int cols) {
		super(start, goal, cells, rows, cols);
		// TODO Auto-generated constructor stub
	}

	protected void initHeapCompareFunction(){
    	open = new BinaryHeap<State>(new Comparator<State>() { 
            public int compare(State s1, State s2) {
                return s2.getExpectValue() - s1.getExpectValue();
            }});
    }
}
