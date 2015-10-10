package edu.rudcs.gridworld.util;

import java.util.Set;



public class State {
	
	private static final int COLUMN = 0x000003FF;
	private static final int ROW = 0x000FFC00;
	private static final int EXPECT = 0x7FF00000;
	
	private int row;
	private int col;
	private boolean bSearch;
	private boolean canMove;
	private int expectCost;
	

	
    public State(int row, int col,byte type){
    	this.row = row;
    	this.col = col;
    	if(type != 1){
    		this.canMove = true;
    	}
    	else{
    		this.canMove = false;
    	}
    	bSearch = false;
    	expectCost = Integer.MAX_VALUE;
    }
    
    public int getExpectValue(){
    	return this.expectCost;
    }
    
    public void setExpectValue(int total){
    	this.expectCost = total;
    }
    
    public void setExpectValue(int estimate, int cost){
    	this.expectCost = 300*(estimate+cost) - cost;
    }
    public void setExpectBackValue(int estimate, int cost){
    	this.expectCost = 300*(estimate+cost) - estimate;
    }
    
    public int getRow(){
    	return this.row;
    }
    
    public int getCol(){
    	return this.col;
    }
    
    public void SetStatus(boolean search){
    	this.bSearch = search;
    }
    
    public boolean getStatus(){
    	return this.bSearch;
    }
    
    public boolean canMove(){
    	return this.canMove;
    }
    
    public void setMove(byte type){
    	if(type != 1){
    		this.canMove = true;
    	}
    	else{
    		this.canMove = false;
    	}
    }
    
    /*
    public boolean getType(){
    	return this.type;
    }
    */
    
    /*
    public void SetType(byte type){
    	if(type != 1){
    		this.type = false;
    	}
    	else{
    		this.type = true;
    	}
    }*/
    
    public boolean equal(State state) {
    	if(row == state.getRow() && col == state.getCol())
    		return true;
    	return false;
	}
    
    

}
