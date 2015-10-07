package edu.rudcs.gridworld.util;

import java.util.Set;

public class State {
	
	private int row;
	private int col;
	private boolean bSearch;
	private byte type;
	private int expectCost;
	

	
    public State(int row, int col,byte type){
    	this.row = row;
    	this.col = col;
    	this.type = type;
    	bSearch = false;
    	expectCost = Integer.MAX_VALUE;
    }
    
    public int getExpectValue(){
    	return this.expectCost;
    }
    
    public void setExpectValue(int total){
    	this.expectCost = total;
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
    
    public byte getType(){
    	return this.type;
    }
    
    public void SetType(byte type){
    	this.type = type;
    }
    
    public boolean equal(State state) {
    	if(row == state.getRow() && col == state.getCol())
    		return true;
    	return false;
	}
    
    

}
