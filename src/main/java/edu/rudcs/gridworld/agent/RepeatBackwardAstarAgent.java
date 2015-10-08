package edu.rudcs.gridworld.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import edu.rudcs.gridworld.util.State;
import edu.rudcs.gridworld.util.TreeNode;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class RepeatBackwardAstarAgent extends RepeatAstarAgent {

	public RepeatBackwardAstarAgent(State start, State goal, byte[][] cells, int rows, int cols) {
		super(start, goal, cells, rows, cols);
		// TODO Auto-generated constructor stub
	}
	
	
	protected boolean computePath(){
		System.out.println("repeat backward");
		
        counter++;
       	
    	cost.put(current, Integer.MAX_VALUE);
    	search.put(current, counter);
    	
    	cost.put(goal, 0);
    	search.put(goal, counter);
    	
    	open.clear();
    	
    	/////
    	close.clear();
    	/////
    	
    	goal.setExpectValue(manhattanDistance(goal,current),cost.get(goal));
    	open.add(goal);
    	
    	tree.put(goal, new TreeNode<State>(goal));
    	while(!open.isEmpty() && cost.get(current) > open.peek().getExpectValue()){
    		State s = open.poll();
    		System.out.println("mahatton of s:"+s.getExpectValue());
    		System.out.println("cost of current:" + cost.get(current));
    		TreeNode<State> curNode = tree.get(s);
    		List<State> successors = getSuccesors(s);
    		
    		////
    		if(close.contains(s)){
				continue;
			}	
			close.add(s);
			expandCounter++;
			showExplore(s);
			////
			
			
    		
    		for(State succ : successors){
    			/////
    			exploreCounter++;
    			/////
    			
    			if(!search.containsKey(succ) || search.get(succ) < counter ){
    				cost.put(succ, Integer.MAX_VALUE);
    		    	search.put(succ, counter);
    			}
    			
    		
    			
    			if(cost.get(succ) > cost.get(s) + PACECOST){
    				cost.put(succ,cost.get(s) + PACECOST);
    				TreeNode<State> child = new TreeNode<State>(succ);
    				curNode.addChild(child);
    				tree.put(succ, child);
    				succ.setExpectValue(manhattanDistance(succ,current),cost.get(succ));
    				if(open.contains(succ)){
    					open.remove(succ);
    				}
    				open.add(succ);
    			}
    			
    		}
    		
    	}
    	
    	if(open.isEmpty()){
    		return false;
    	}
    	
    	showPath();
		
		return true;
	}
	
	protected void showPath(){
        Grid<Actor> grid = getGrid();
    	
    	TreeNode<State> node = tree.get(current).getParent();
    	
    	if(node == null)
    		return;
    	
    	List<State> tmp = new ArrayList<State>();
    	do{
    		State s = node.getData();
    		grid.putColor(new Location(s.getRow(),s.getCol()), Agent.PATH_COLOR);
    		tmp.add(s);
    		node = node.getParent();
    		
    	}while(node != null);
    	
    	for(int i = tmp.size() - 1; i >= 0 ; i--){
    		
    		path.push(tmp.get(i));
    		
    	}
    	
	}
	
}
