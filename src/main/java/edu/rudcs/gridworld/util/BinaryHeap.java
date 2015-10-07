package edu.rudcs.gridworld.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinaryHeap<T> {
	
	
	private List<T> heap ;
	private Comparator<? super T> c;
	
	public BinaryHeap(Comparator<? super T> c){
		heap = new ArrayList<T>();
		this.c = c;
	}
	
	public BinaryHeap(List<T> heap,Comparator<? super T> c){
		this.heap = heap;
		this.c = c;
		buildHeap(heap.size());
	}
	
	protected void buildHeap(int length){
		for(int i = length/2 -1; i >= 0 ; i--){
			heapify(i);
		}
	}
	
	protected void sort(){
		buildHeap(heap.size());
		for(int i = heap.size() - 1; i > 0; i--){
			swap(i,0);
			heapify(0,i);
		}
		
		
	}
	
	public void add(T data){
		heap.add(data);
	
		buildHeap(heap.size());
	}
	
	protected int left(int i){
		return 2*(i+1) - 1;
	}
	
	protected int right(int i){
		return 2*(i+1);
	}
	
	private void heapify(int i){
		heapify(i,heap.size());
	}
	
	private void heapify(int i, int size){
		
		int l = left(i);
		int r = right(i);
		int father;
		if(l < size && c.compare( heap.get(l), heap.get(i)) > 0){
			father = l;
		}
		else
			father = i;
		if(r < size && c.compare(heap.get(r), heap.get(father)) > 0){
			father = r;
		}
		if(father != i){
			swap(father,i);
			heapify(father,size);
		}
	}
	
	public T top(){
		return heap.get(0);
	}
	
	public T extractTop(){
		if(heap.isEmpty()){
			throw new IllegalArgumentException("can not extract max element in empty heap"); 
		}
		
		T max = heap.get(0);
		heap.remove(0);
		buildHeap(heap.size());
		return max;
	}
	
	private void swap(int i , int j){
		
		T tmp;
		tmp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, tmp);
		
	}
	
	public boolean contains(T data){
		return heap.contains(data);
	}
	
	public boolean remove(T data){
		 boolean ret = heap.remove(data);
		 if(!ret) return false;
		 buildHeap(heap.size());
		 return true;
	}
	
	public boolean update(T data){
		boolean ret = false;
		if(heap.contains(data)){
			ret = heap.remove(data);
		}
		if(!ret) return ret;
		ret = heap.add(data);
		buildHeap(heap.size());
		return ret;
	}
	
	public void clear(){
		heap.clear();
	}
	
	public boolean isEmpty(){
		return heap.isEmpty();
	}
	
	public T peek(){
		return heap.get(0);
	}
	
	public T poll(){
		return extractTop();
	}

	public static void main(String args[]){
		      
	        ArrayList<State> list = new ArrayList<State>();
	       
	        State s1 = new State(1,1,(byte)1); 
	        State s2 = new State(1,1,(byte)1); 
	        State s3 = new State(1,1,(byte)1); 
	        State s4 = new State(1,1,(byte)1); 
	        State s5 = new State(1,1,(byte)1); 
	        State s6 = new State(1,1,(byte)1); 
	        s1.setExpectValue(4);
	        s2.setExpectValue(14);
	        s3.setExpectValue(12);
	        s4.setExpectValue(19);
	        s5.setExpectValue(10);
	        s6.setExpectValue(6);
	        list.add(s1);
	        list.add(s2);
	        list.add(s3);
	        list.add(s4);
	        list.add(s5);
	        list.add(s6);
	 
	        
	        
	        BinaryHeap<State> heap = new BinaryHeap<State>(list, new Comparator<State>() { 
	            public int compare(State s1, State s2) {
	                return s1.getExpectValue() - s2.getExpectValue();
	            }});
	          
	        
	        for(State s : list){
	        	System.out.print(s.getExpectValue() + " ");  
	        }
	        
	        System.out.println();  
	          
	        heap.sort();  
	        for (State s : list) {  
	            System.out.print(s.getExpectValue() + " ");  
	        }  
	        System.out.println(); 
	        
	        System.out.println(list.contains(s1));  
	        list.remove(s1);
	        
	        for(State s : list){
	        	System.out.print(s.getExpectValue() + " ");  
	        }
	        
	        System.out.println();  
	          
	        heap.sort();  
	        for (State s : list) {  
	            System.out.print(s.getExpectValue() + " ");  
	        }  
	        System.out.println(); 
	        
	        State s7 = new State(1,1,(byte)1); 
	        s7.setExpectValue(40);
	        list.add(s7);
	        
	        for(State s : list){
	        	System.out.print(s.getExpectValue() + " ");  
	        }
	        
	        System.out.println();  
	          
	        heap.sort();  
	        for (State s : list) {  
	            System.out.print(s.getExpectValue() + " ");  
	        }  
	        System.out.println(); 
	        
	        /*
	        for (int i : list) {  
	            System.out.print(i + " ");  
	        }  
	        System.out.println();  
	          
	        heap.sort();  
	        for (int i : list) {  
	            System.out.print(i + " ");  
	        }  
	        System.out.println(); 
	        
	        heap.add(20);
	        
	        
	        for (int i : list) {  
	            System.out.print(i + " ");  
	        }  
	        System.out.println();  
	          
	        heap.sort();  
	        for (int i : list) {  
	            System.out.print(i + " ");  
	        }  
	        System.out.println(); 
	        
	        heap.remove(9);
	        for (int i : list) {  
	            System.out.print(i + " ");  
	        }  
	        System.out.println();  
	          
	        heap.sort();  
	        for (int i : list) {  
	            System.out.print(i + " ");  
	        }  
	        System.out.println(); 
	        */
	}
	
    
}
