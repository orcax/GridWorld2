package edu.rudcs.gridworld.agent;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.PriorityBlockingQueue;

import edu.rudcs.gridworld.util.State;
import edu.rudcs.gridworld.util.TreeNode;

public class RepeatForwardAstarAgent extends RepeatAstarAgent {

	public RepeatForwardAstarAgent(State start, State goal, byte[][] cells, int rows, int cols) {
		super(start, goal, cells, rows, cols);
	}
	
	protected boolean computePath(){
    	//System.out.println("repeat forward");
		
		
		
    	counter++;
    	
  
    	cost.put(current, 0);
    	search.put(current, counter);
    	
    	cost.put(goal, Integer.MAX_VALUE);
    	search.put(goal, counter);
    	
    	
    	
    	open.clear();
    	
    	/////
    	close.clear();
    	/////
    	
    	current.setExpectValue(manhattanDistance(current,goal)+cost.get(current));
    	open.add(current);
    	
    	tree.put(current, new TreeNode<State>(current));
    	while(!open.isEmpty() && cost.get(goal) > open.peek().getExpectValue()){
    		State s = open.poll();
    		//System.out.println("mahatton of s:"+s.getExpectValue());
    		//System.out.println("cost of goal:" + cost.get(goal));
    		
    		
    		
    		TreeNode<State> curNode = tree.get(s);;
    		
    		/*
    		if(close.contains(s)){
				continue;
			}	*/
			close.add(s);
			expandCounter++;
			showExplore(s);
			/////
    		
    		List<State> successors = getSuccesors(s);
    		
    		for(State succ : successors){
    			
    			
    			
    			
    			if(!search.containsKey(succ) || search.get(succ) < counter ){
    				cost.put(succ, Integer.MAX_VALUE);
    		    	search.put(succ, counter);
    			}
    			
    			
    			
    			if(cost.get(succ) > cost.get(s) + PACECOST){
    				exploreCounter++;
    				cost.put(succ,cost.get(s) + PACECOST);
    				
	    			TreeNode<State> child = new TreeNode<State>(succ);
	    			curNode.addChild(child);
	    			tree.put(succ, child);
    				
    				succ.setExpectValue(manhattanDistance(succ,goal)+cost.get(succ));
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
	
	
	

}
