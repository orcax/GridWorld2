package edu.rudcs.gridworld.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rudcs.gridworld.util.State;
import edu.rudcs.gridworld.util.TreeNode;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class AdaptiveAstarAgent extends RepeatAstarAgent {

	protected Map<State,Integer> newEstimates;
	
	protected Map<State,Integer> expands;
	
	public AdaptiveAstarAgent(State start, State goal, byte[][] cells, int rows, int cols) {
		super(start, goal, cells, rows, cols);
		
		newEstimates = new HashMap<State,Integer>();
		expands = new HashMap<State,Integer>();
	}
	
	
	
	protected boolean computePath(){
    	System.out.println("repeat forward");
    	
    	counter++;
    	
    	tree.clear();
    	expands.clear();
    	
    	
    	cost.put(current, 0);
    	search.put(current, counter);
    	
    	cost.put(goal, Integer.MAX_VALUE);
    	search.put(goal, counter);
    	
    	
    	
    	open.clear();
    	current.setExpectValue(calcDist(current)+cost.get(current));
    	open.add(current);
    
    	
    	tree.put(current, new TreeNode<State>(current));
    	while(!open.isEmpty() && cost.get(goal) > open.peek().getExpectValue()){
    		State s = open.poll();
    		showExplore(s);
    		expands.put(s, 0);
    		
    		System.out.println("mahatton of s:"+s.getExpectValue());
    		System.out.println("cost of goal:" + cost.get(goal));
    		TreeNode<State> curNode = tree.get(s);
    		List<State> successors = getSuccesors(s);
    		for(State succ : successors){
    			
    				
    			
    			if(!search.containsKey(succ) || search.get(succ) < counter ){
    				cost.put(succ, Integer.MAX_VALUE);
    		    	search.put(succ, counter);
    			}
    			
    		
    			
    			if(cost.get(succ) > cost.get(s) + PACECOST){
    				cost.put(succ,cost.get(s) + PACECOST);
    				TreeNode<State> child = new TreeNode<State>(succ);
    				curNode.addChild(child);
    				tree.put(succ, child);
    				succ.setExpectValue(calcDist(succ)+ cost.get(succ));
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
    	TreeNode<State> node = tree.get(goal);
    	int i = 0;
    	while(node.getParent() != null){
    		State s = node.getData();
    		grid.putColor(new Location(s.getRow(),s.getCol()), Agent.PATH_COLOR);
    		path.push(s);
    		if(expands.containsKey(s)){
    			estimate.put(s, i);
    		}
    		node = node.getParent();
    		i++;
    	}
    }
    
   
    
    protected int calcDist(State from){
    	if(newEstimates.containsKey(from)){
    		return newEstimates.get(from);
    	}
    	else
    	    return Math.abs(from.getRow() - goal.getRow())
                    + Math.abs(from.getCol() - goal.getCol());
    	
    }

}
