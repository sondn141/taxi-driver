package edu.hust.soict.cbls.algorithm.backtrack;

import java.util.ArrayList;

import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Point;
import edu.hust.soict.cbls.entity.Taxi;

public class Driver {
	private Taxi taxi;
	private ArrayList<Point> tour;
	private double length = 0.0;
	private double weight = 0.0;
	
	public Driver(Taxi taxi) {
		this.taxi = taxi;
		this.tour = new ArrayList<>();
		this.tour.add(taxi.startPoint());
	}
	
	public ArrayList<Point> getTour() {
		return tour;
	}
	
	private double delta(Point q) {
		Point p = tour.get(tour.size() - 1);
		return p.distance(q) + q.distance(taxi.stopPoint()) 
		- p.distance(taxi.stopPoint());
	}
	
	public void forward(Point q) {
		length += delta(q);
		tour.add(q);
	}
	
	public void backward() {
		Point q = tour.get(tour.size() - 1);
		tour.remove(tour.size() - 1);
		length -= delta(q);
	}
	
	public boolean pickup(Commodity c) {
		if (weight + c.getWeight() > taxi.getCap())
			return false;
		weight += c.getWeight();
		forward(c.getPickup());
		return true;
	}
	
	public void pickupBack(Commodity c) {
		weight -= c.getWeight();
		backward();
	}
	
	public void deliver(Commodity c) {
		weight -= c.getWeight();
		forward(c.getDeliver());
	}
	
	public void deliverBack(Commodity c) {
		weight += c.getWeight();
		backward();
	}
	
	public void serve(Passenger p) {
		forward(p.getGetIn());
		forward(p.getGetOff());
	}
	
	public void serveBack(Passenger p) {
		backward();
		backward();
	}
	
	public double getLength() {
		return length;
	}
}
