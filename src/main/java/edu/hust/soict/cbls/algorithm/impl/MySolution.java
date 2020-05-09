package edu.hust.soict.cbls.algorithm.impl;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.backtrack.Driver;
import edu.hust.soict.cbls.common.datastructure.Point;

import java.util.ArrayList;
import java.util.List;

public class MySolution implements Solution {
	private ArrayList<List<Integer>> solution;
	private double score;
	
	public MySolution() {
		score = Double.MAX_VALUE;
	}
	
	public void setSolution(ArrayList<Driver> tours) {
		ArrayList<List<Integer>> solution = new ArrayList<>();
    	for (Driver driver: tours) {
    		ArrayList<Point> tour = driver.getTour();
    		ArrayList<Integer> indices = new ArrayList<>();
    		for (Point p: tour)
    			indices.add(p.getIdx());
    		solution.add(indices);
    	}
    	this.solution = solution;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
    @Override
    public List<List<Integer>> convert() {
        return solution;
    }

    @Override
    public double score() {
        return score;
    }
}
