package edu.rudcs.gridworld.agent;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.PriorityBlockingQueue;

import edu.rudcs.gridworld.map.Shadow;
import edu.rudcs.gridworld.util.BinaryHeap;
import edu.rudcs.gridworld.util.State;
import edu.rudcs.gridworld.util.TreeNode;
import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;



public class RepeatAstarAgent extends Agent {
	
    protected int PACECOST = 1;
	
	protected State start;
	protected State current;
    protected State goal;
    protected boolean bStart;
    protected int counter;
    //protected Queue<State> open;
    protected BinaryHeap<State> open;
	protected Set<State> close;

    protected int rows,cols;
    
    
    protected State[][] states;
    
    
    Map<State,Integer> cost;
    Map<State,Integer> estimate;
    Map<State,Integer> search;
    Map<State,TreeNode<State>> tree;
    
    
    //TreeNode<State> root;
    Stack<State> path;
    
    Grid<Actor> grid;
    
    protected int expandCounter;
    protected int exploreCounter;
    protected int stepCounter;
    
    
    
    public RepeatAstarAgent(State start, State goal, byte[][] cells, int rows , int cols){
    	
    	states = new State[rows][cols];
    	
    	
    	this.rows = rows;
    	this.cols = cols;
    	
    	search = new HashMap<State,Integer>();
    	cost = new HashMap<State,Integer>();
    	estimate = new HashMap<State,Integer>();
    	//root = new TreeNode<State>(current);
    	tree = new HashMap<State,TreeNode<State>>();
    	path = new Stack<State>();
    	counter = 0;
    	expandCounter = 0;
    	exploreCounter = 0;
    	stepCounter = 0;
    	
    	
    	for(int i = 0; i < rows; i++){
    		for(int j = 0; j < cols; j++){
    			states[i][j] = new State(i,j,cells[i][j]);
    			//System.out.print(cells[i][j] + "|");
    			states[i][j].SetStatus(false);
    			//System.out.print(states[i][j].getRow() + "|" + states[i][j].getCol() + " ");
    		    if(i == start.getRow() && j == start.getCol()){
    		    	this.current = states[i][j];
    		    	this.start = this.current;
    		    }
    		    else if(i == goal.getRow() && j == goal.getCol()){
    		    	this.goal = states[i][j];
    		    }
    		}
    		//System.out.println("");
    	}
    	
    	
    	
    	bStart = false;
    	
    	/*
    	
    	open = new PriorityBlockingQueue<State>(100,
                new Comparator<State>() {
            
            public int compare(State s1, State s2) {
                return s1.getExpectValue() - s2.getExpectValue();
            }});
    
    	*/
    	open = new BinaryHeap<State>(new Comparator<State>() { 
            public int compare(State s1, State s2) {
                return s2.getExpectValue() - s1.getExpectValue();
            }});
    	
    	close = new HashSet<State>();
    	
    	
    	grid = getGrid();
    	
    }
    
    public void updateExplore(){
    	
    	int row = current.getRow();
    	int col = current.getCol();
    	
    	if(col + 1 < cols){
    	    states[row][col+1].SetStatus(true);
    	}
    	if(col - 1 >= 0){
    		states[row][col-1].SetStatus(true);
    	}
    	if(row + 1 < rows){
    		states[row+1][col].SetStatus(true);
    	}
    	if(row - 1 >= 0){
    		states[row-1][col].SetStatus(true);
    	}
    	
    	
    	
    	
    	
    }
    
    
    public void act(){
    		
    	
    	if(testGoal()){
    		if(end == true){
        		return;
        	}
    		success();
    		return;
    	}
    	//System.out.println("act");
    	if(!bStart){
    		bStart = true;
    		updateExplore();
    		if(!computePath()){
    			noPath();
    		}
    		
    		return;
    	}
    	if(canMove()){
    		//System.out.println("move");
    		move();
    	}
    	else{
    		clearPath();
    		if(!computePath()){
    			noPath();
    			return;
    		}
    		else{
    			if(canMove()){
    			    move();
    			}
    			else{
    				clearPath();
    				noPath();
    			    return;
    			}
    		}
    	}
    }
    
    protected boolean testGoal(){
    	
    	if(current.equal(goal)){
    		return true;
    	}
    	
    	return false;
    	
    }
    
    protected void noPath(){
    	if(end) return;
    	end = true;
    	bStart = false;
    	//Fail|total expandCounter|total exploreCounter|total step
    	String sb = "F|" + expandCounter
    			+"|" + exploreCounter
    			+"|" + stepCounter + "\n";
    	System.out.print(sb);
    	try{
    	    record("record/",getClass().getName(),sb);
    	}
    	catch(IOException e){
    		
    	}
    }
    
    protected void success(){
    	end = true;
    	//success|total expandCounter|total exploreCounter|total step|right path long
    	String sb = "S|" + expandCounter
    			+"|" + exploreCounter
    			+"|" + stepCounter 
    			+ "|";
    	int length = findOriginalPathLength();
    	sb = sb + length + "\n";
    	System.out.print(sb);
    	try{
    	    record("record/",getClass().getName(),sb);
    	}
    	catch(IOException e){
    		
    	}
    }
    
    protected void clear(){
    	tree.clear();
    	
    }
    
    protected int findOriginalPathLength(){
    	current = start;
    	
    	for(int k = 0; k < rows; k++){
    		for(int j = 0; j < cols; j++){
    			states[k][j].SetStatus(true);
    		}
    	}
    	clear();
    	computePath();
    	return showRightPath();
    	
    }
    
    protected int showRightPath(){
    	
    	Grid<Actor> grid = getGrid();
    	TreeNode<State> node = tree.get(goal);
    	int i = 0;
    	while(node.getParent() != null){
    		State s = node.getData();
    		grid.putColor(new Location(s.getRow(),s.getCol()), Color.RED);
    		node = node.getParent();
    		i++;
    	}
    	current = goal;
    	return i;
    	
    }
    
    protected void record(String filename, String mapName,String content) throws IOException{
    	FileWriter fw = null; 
    	PrintWriter pw = null;
    	 
         try {
        	 
        	 File file = new File(filename+mapName+ManagementFactory.getRuntimeMXBean().getStartTime());  
        	 if(!file.exists()){
        		 file.createNewFile();
        	 }
        	 fw = new FileWriter(file,true);
             pw = new PrintWriter(fw);
             pw.append(content.toString());
         } catch (FileNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (UnsupportedEncodingException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } finally {
             if (pw != null) {
                 pw.close();
             }
             if(fw != null){
            	 fw.close();
             }
             
         }
    }
    
    protected void move(){
    	State next = getNextNode();
    	if(next != null && next.canMove()){
    	    moveTo(chgLocation(next));
    	    current = next;
    	    updateExplore();
    	}
    }
    
    public void moveTo(Location loc) {
        Grid<Actor> grid = getGrid();
        if (grid == null) {
            return;
        }
        Location oldLoc = getLocation();
        super.moveTo(loc);
        Shadow shadow = new Shadow(Location.NORTH);
        shadow.putSelfInGrid(grid, oldLoc);
        grid.putColor(oldLoc, Color.green);
        stepCounter++;
    }

    
    protected Location chgLocation(State next){
    	Location loc = new Location(next.getRow(),next.getCol());
    	return loc;
    }
    
    
    protected List<State> getSuccesors(State s){
        
    	List<State> succesors = new ArrayList<State>();
    	int row = s.getRow();
    	int col = s.getCol();
    	
    	if(row + 1 < rows && 
    			(states[row+1][col].canMove() || !states[row+1][col].getStatus())){
    		succesors.add(states[row+1][col]);
    	}
    	if(row - 1 >= 0 && 
    			(states[row-1][col].canMove() || !states[row-1][col].getStatus())){
    		succesors.add(states[row-1][col]);
    	}
    	if(col + 1 < cols &&
    			(states[row][col+1].canMove()|| !states[row][col+1].getStatus())){
    		succesors.add(states[row][col+1]);
    	}
    	if(col - 1 >= 0 && 
    			(states[row][col-1].canMove() || !states[row][col-1].getStatus())){
    		succesors.add(states[row][col-1]);
    	}
        return succesors;
    }
    
    protected List<State> getRandSuccesors(State s){
    	List<State> succesors = new ArrayList<State>();
    	int row = s.getRow();
    	int col = s.getCol();
    	
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	int count = 4;
    	Random r = new Random();
    	while(list.size() < count){
    		int i = r.nextInt(4);
    		if(!list.contains(i)){
    			list.add(i);
    		}
    	}
    	
    	for(int i = 0; i < count; i++){
    		switch(list.get(i)){
    		case 0:
    			if(row + 1 < rows && 
    	    			(states[row+1][col].canMove()|| !states[row+1][col].getStatus())){
    	    		succesors.add(states[row+1][col]);
    	    	}
    			break;
    		case 1:
    			if(row - 1 >= 0 && 
    	    		(states[row-1][col].canMove()|| !states[row-1][col].getStatus())){
    	        	succesors.add(states[row-1][col]);
            	}
    			break;
    		case 2:
    			if(col + 1 < cols &&
    	    			(states[row][col+1].canMove()|| !states[row][col+1].getStatus())){
    	    		succesors.add(states[row][col+1]);
    	    	}
    			break;
    		case 3:
    			if(col - 1 >= 0 && 
    	    	    	(states[row][col-1].canMove()|| !states[row][col-1].getStatus())){
            		succesors.add(states[row][col-1]);
            	}
    			break;
    		}
    	}
    	

        return succesors;
    }
    
    protected List<State> getSuccesorFromTree(State s){
    	List<State> succesors = new ArrayList<State>();
    	int row = s.getRow();
    	int col = s.getCol();
    	TreeNode<State> node = tree.get(s);
    	for(TreeNode<State> succNode : node.getChildren()){
    		State succ = succNode.getData();
    		if(succ.canMove()){
    			succesors.add(succ);
    		}
    	}
    	return succesors;
    	
    }
    
    protected boolean computePath(){
    	
    	return true;
    }
    
    protected void showPath(){
    	Grid<Actor> grid = getGrid();
    	
    	TreeNode<State> node = tree.get(goal);
    	while(node.getParent() != null){
    		State s = node.getData();
    		grid.putColor(new Location(s.getRow(),s.getCol()), Agent.PATH_COLOR);
    		path.push(s);
    		node = node.getParent();
    	}
    	//
    	//
    	
    }
    
    protected void clearPath(){
    	Grid<Actor> grid = getGrid();
    	while(!path.isEmpty()){
    		State s = path.pop();
    		grid.putColor(new Location(s.getRow(),s.getCol()), Color.GREEN);
    	}
    }
    
    protected State getNextNode(){
    	if(path.empty()){
    		return null;
    	}
    	else{
	    	State next = path.pop();
	    	return next;
    	}
    }
    
    public boolean canMove(){
    	if(path.empty()){
    		return false;
    	}
    	else{
	    	State next = path.peek();
	    	
	    	if(next == null){
	    		return false;
	    	}
	    	
	    	if(next.canMove()){
	    		return true;
	    	}
	    	return false;
    	}
    }
    
    protected void showExplore(State s){
    	
    	grid = getGrid();
    	grid.putColor(new Location(s.getRow(),s.getCol()), Color.green);
    	
    }
    
    
    
    protected int manhattanDistance(State from, State to){
    	return Math.abs(from.getRow() - to.getRow())
                + Math.abs(from.getCol() - to.getCol());
    	
    }
    


}
