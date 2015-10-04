package edu.rudcs.gridworld.map;
import java.util.ArrayList;
import java.util.List;

import edu.rudcs.gridworld.util.State;
import info.gridworld.grid.Location;

public class RepeatAstarSearchMap extends SearchMap {
	
	protected byte[][] cells; 
	protected List<State> goal;
	protected List<State> start;
	
	public void loadMap(String path){
		
		super.loadMap(path);
		
		cells = new byte[rows][cols];
		
		byte typeValue = -1;
		
		start = new ArrayList<State>();
		goal = new ArrayList<State>();
		
		
		for (ActorType at : objects.keySet()) {
			
			switch(at){
			
			case ROAD: typeValue = 0;break;
			
			case WALL: typeValue = 1;break;
			
			case SHADOW: typeValue = 2;break;
			
			case AGENT: typeValue = 3;break;
			
			case GOAL: typeValue = 4;break;
			
			}
			
            List<Location> locs = objects.get(at);
            
            for (Location l : locs) {
            	
                cells[l.getRow()][l.getCol()] = typeValue;
                
                if(typeValue == 3){
                	start.add(new State(l.getRow(),l.getCol(),(byte)3));
                }
                else if(typeValue == 4){
                	goal.add(new State(l.getRow(),l.getCol(),(byte)4));
                }
            }
        }
	
	}
	
	public List<State> getGoals(){
		return goal;
	}
	
	public List<State> getStarts(){
		return start;
	}
	
	public int getRows(){
		return rows;
	}

	public int getColumn(){
		return cols;
	}
	
	public byte[][] getCells(){
		return cells;
	}
}
