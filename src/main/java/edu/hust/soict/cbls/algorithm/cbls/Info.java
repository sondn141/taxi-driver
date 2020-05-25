package edu.hust.soict.cbls.algorithm.cbls;

import java.util.ArrayList;
import java.util.List;

import edu.hust.soict.cbls.algorithm.Input;
import localsearch.domainspecific.vehiclerouting.vrp.entities.Point;

public class Info {
	public int K, N, M;
	private int size;
	public ArrayList<Point> points;
	public double[] weights;
	public double[][] distances;
	
	public Info(Input input) {
		K = input.getTaxis().size();
		N = input.getPassengers().size();
		M = input.getCommodities().size();
		size = 2*N+2*M+2*K+1;

		points = new ArrayList<>();
    	for (edu.hust.soict.cbls.entity.Point p: input.getPoints())
    		points.add(p.toCBLS());
    	edu.hust.soict.cbls.entity.Point station = input.getPoints().get(0);
    	for (int k=1; k<=K; k++)
    		points.add(new Point(2*N+2*M+k, station.getX(), station.getY()));
    	for (int k=1; k<=K; k++)
    		points.add(new Point(2*N+2*M+K+k, station.getX(), station.getY()));
    	
    	weights = new double[size];
    	for (int k=1; k<=K; k++)
    		weights[2*N+2*M+k] = input.getTaxi(k-1).getCap();
    	for (int i=0; i<M; i++) {
    		double w = input.getCommodities().get(i).getWeight();
    		weights[i+N+1] = -w;
    		weights[i+2*N+M+1] = w;
    	}
    	
    	double[][] distanceMat = input.getDistanceMat();
    	distances = new double[size][size];
    	for (int i=0; i<=2*N+2*M; i++)
    		for (int j=0; j<=2*N+2*M; j++)
    			distances[i][j] = distanceMat[i][j];
    	for (int i=2*N+2*M+1; i<size; i++)
    		for (int j=1; j<=2*N+2*M; j++)
    			distances[i][j] = distances[j][i] = distanceMat[0][j];
	}
	
	public List<Point> startPoints() {
		return points.subList(2*N+2*M+1, 2*N+2*M+K+1);
	}
	
	public List<Point> stopPoints() {
		return points.subList(2*N+2*M+K+1, size);
	}
	
	public List<Point> clientPoints() {
		return points.subList(1, 2*N+2*M+1);
	}
	
	public List<Point> goodPickupPoints() {
		return points.subList(N+1, N+M+1);
	}
	
	public ArrayList<Point> allPoints() {
		return new ArrayList(points.subList(1, size));
	}
}
