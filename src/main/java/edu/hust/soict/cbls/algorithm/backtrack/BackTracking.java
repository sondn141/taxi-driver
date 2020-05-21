package edu.hust.soict.cbls.algorithm.backtrack;

import java.util.ArrayList;
import java.util.Arrays;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Taxi;
import edu.hust.soict.cbls.algorithm.impl.MySolution;
import edu.hust.soict.cbls.common.config.Properties;

public class BackTracking extends Solver {
	private MySolution solution;
	private ArrayList<Driver> drivers;
	private Passenger[] passengers;
	private Commodity[] commodities;
	
	
	private int[] commodityStatus;
	private boolean[] passengerStatus;
	private int N, M, K;
	private double minEdge;
	
    public BackTracking(Properties props) {
        super(props);
        M = input.getCommodities().size();
        N = input.getPassengers().size();
        K = input.getTaxis().size();
        passengers = input.getPassengers().toArray(new Passenger[N]);
        commodities = input.getCommodities().toArray(new Commodity[M]);
        
        double[][] edges = input.getDistanceMat();
        minEdge = Double.MAX_VALUE;
        for (int i=0; i<edges.length; i++)
        	for (int j=i+1; j<edges.length; j++)
        		if (edges[i][j] < minEdge)
        			minEdge = edges[i][j];
    }
    
    private void backtrack(int step, int k, double score) {
//    	System.out.println(step + " " + k + " " + score);
    	if (score + (2*N+2*M-step) * minEdge >= solution.score())
    		return;
    	if (step == 2*N + 2*M) {
//    		System.out.println(score);
    		solution.setScore(score);
    		solution.convertSolution(drivers);
    		return;
    	}
    	Driver driver = drivers.get(k);
    	double length = driver.getLength();
    	
    	for (int i=0; i<N; i++)
    		if (!passengerStatus[i]) {
    			driver.serve(passengers[i]);
    			passengerStatus[i] = true;
    			
    			backtrack(step + 2, k, score + driver.getLength() - length);
    			
    			driver.serveBack(passengers[i]);
    			passengerStatus[i] = false;
    		}
    	
    	for (int i=0; i<M; i++)
    		if (commodityStatus[i] == -1) {
    			if (!driver.pickup(commodities[i]))
    				continue;
    			commodityStatus[i] = k;
    			
    			backtrack(step + 1, k, score + driver.getLength() - length);
    			
    			driver.pickupBack(commodities[i]);
    			commodityStatus[i] = -1;
    		}
    		else if (commodityStatus[i] == k) {
    			driver.deliver(commodities[i]);
    			commodityStatus[i] = -2;
    			
    			backtrack(step + 1, k, score + driver.getLength() - length);
    			
    			driver.deliverBack(commodities[i]);
    			commodityStatus[i] = k;
    		}
    	
    	if (k < K - 1)
    		backtrack(step, k + 1, score);
    }

    @Override
    public Solution solve() {
    	solution = new MySolution();
    	drivers = new ArrayList<>();
    	for (Taxi taxi: input.getTaxis())
    		drivers.add(new Driver(taxi));
    	
    	passengerStatus = new boolean[N];
    	commodityStatus = new int[M];
    	Arrays.fill(commodityStatus, -1);
    	
    	backtrack(0, 0, 0.0);
        return solution;
    }
}
