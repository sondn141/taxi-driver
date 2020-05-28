package edu.hust.soict.cbls.algorithm.impl;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.backtrack.Driver;
import edu.hust.soict.cbls.entity.Point;

import java.util.ArrayList;
import java.util.List;

public class MySolution implements Solution {
	private List<List<Integer>> solution;
	private double score;
	
	public MySolution() {
		score = Double.MAX_VALUE;
	}
	
	public void convertSolution(ArrayList<Driver> tours) {
		ArrayList<List<Integer>> solution = new ArrayList<>();
    	for (Driver driver: tours) {
    		ArrayList<Point> tour = driver.getTour();
    		ArrayList<Integer> indices = new ArrayList<>();
    		for (Point p: tour)
    			indices.add(p.getIdx());
    		indices.add(0);
    		solution.add(indices);
    	}
    	this.solution = solution;
	}
	
	public void validate(Input input) {
		double[][] d = input.getDistanceMat();
		double score = 0.0;
		for (List<Integer> lst: solution) {
			for (int i=0; i<lst.size()-1; i++)
				score += d[lst.get(i)][lst.get(i+1)];
		}
		System.out.println("Debug score: " + score);
	}
	
	public List<List<Integer>> getSolution() {
		return solution;
	}
	
	public void setSolution(List<List<Integer>> tours) {
		this.solution = tours;
	}
	
	public double getScore() {
		return score;
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
