package edu.rudcs.gridworld.util;

import java.util.Set;



public class State {
	
	private static final int COLUMN = 0x000003FF;
	private static final int ROW = 0x000FFC00;
	private static final int ROWCOL = 0x000FFFFF;
	private static final int EXPECT = 0xFFF00000;
	private static final int EXP = 0x00000FFF;
	
	private static final int ROW_LENGTH = 10;
	private static final int EXP_LENGTH = 20;
	
	//private int row;
	//private int col;
	private boolean bSearch;
	private boolean canMove;
	//private int expectCost;
	
	private int seq;
	
	
    public State(int row, int col,byte type){
    	seq = EXPECT;
    //	this.row = row;
    	seq = seq|col|(row<<ROW_LENGTH);
    //	this.col = col;
    	if(type != 1){
    		this.canMove = true;
    	}
    	else{
    		this.canMove = false;
    	}
    	bSearch = false;
    	
    	//expectCost = Integer.MAX_VALUE;
    }
    
    public int getExpectValue(){
    	int exp = (seq&EXPECT)>>EXP_LENGTH&EXP;
    	return exp;
    }
    
    public void setExpectValue(int total){
    	seq = seq&ROWCOL;
    	seq = seq|((total<<EXP_LENGTH)&EXPECT);
    }
    
    public void setExpectValue(int estimate, int cost){
    	//this.expectCost = 300*(estimate+cost) - cost;
    	int total = 4*(estimate+cost) - cost;
    	seq = seq&ROWCOL;
    	seq = seq|((total<<EXP_LENGTH)&EXPECT);
    }
    public void setExpectBackValue(int estimate, int cost){
    	//this.expectCost = 300*(estimate+cost) - estimate;
    	int total = 4*(estimate+cost) - cost;
    	seq = seq&ROWCOL;
    	seq = seq|((total<<EXP_LENGTH)&EXPECT);
    }
    
    public int getRow(){
    	int row = (seq&ROW)>>ROW_LENGTH;
    	return row;
    }
    
    public int getCol(){
    	int col = seq&COLUMN;
    	return col;
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
    
    
    public boolean equals(State state) {
    	int row = getRow();
    	int col = getCol();
    	if(row == state.getRow() && col == state.getCol())
    		return true;
    	return false;
	}
    
    

}
