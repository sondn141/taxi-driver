package edu.hust.soict.cbls.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Point;
import edu.hust.soict.cbls.entity.Taxi;

import java.util.ArrayList;
import java.util.List;

public class Input {

    private List<Passenger> passengers;
    private List<Commodity> commodities;
    private List<Taxi> taxis;

    private List<Point> points;
    private double[][] distanceMat;

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public List<Taxi> getTaxis() {
        return taxis;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    public void setTaxies(List<Taxi> taxis) {
        this.taxis = taxis;
    }

    public void setTaxi(Taxi t, int index){
        taxis.set(index, t);
    }

    public Taxi getTaxi(int index){
        return taxis.get(index);
    }

    public double[][] getDistanceMat() {
        if(distanceMat == null)
            createDistanceMatrix();
        return this.distanceMat;
    }

    public double distance(int i, int j){
        if(distanceMat == null)
            createDistanceMatrix();
        return distanceMat[i][j];
    }

    private void createDistanceMatrix(){
        if(points == null || points.isEmpty())
            initPoints();

        int n = points.size();
        distanceMat = new double[n][n];
        for(int i = 0 ; i < n - 1 ; i ++){
            for(int j = i + 1 ; j < n ; j ++){
                distanceMat[i][j] = distanceMat[j][i] = points.get(i).distance(points.get(j));
            }
        }
    }

    public Point point(int index){
        if(points == null || points.isEmpty())
            initPoints();
        return points.get(index);
    }
    
    /**
     * @author hungth
     * @since 20200523
     * */
    public List<Point> getPoints() {
    	return points;
    }

    public int size(){
        if(points == null || points.isEmpty())
            initPoints();
        return points.size();
    }


    /**
    * Types:
     * 0: Taxi's station
     * 1: Spots where Passenger gets in
     * 2. Spots where Commodity is picked up
     * 3. Spots where Passenger gets off
     * 4: Spots where Commodity is delivered
    * */
    public int pointType(int index){
        if(index == 0)
            return 0;
        else if(index <= passengers.size())
            return 1;
        else if(index <= passengers.size() + commodities.size())
            return 2;
        else if(index <= 2 * passengers.size() + commodities.size())
            return 3;
        else if(index <= 2 * (passengers.size() + commodities.size()))
            return 4;
        else
            throw new RuntimeException("Point type is not recognized. Index passed: " + index);
    }

    private void initPoints(){
        List<Point> getIn = new ArrayList<>();
        List<Point> getOff = new ArrayList<>();
        List<Point> pickup = new ArrayList<>();
        List<Point> deliver = new ArrayList<>();

        for(Passenger p : passengers){
            getIn.add(p.getGetIn());
            getOff.add(p.getGetOff());
        }

        for(Commodity c : commodities){
            pickup.add(c.getPickup());
            deliver.add(c.getDeliver());
        }

        points = new ArrayList<>();
        points.add(Taxi.station());
        points.addAll(getIn);
        points.addAll(pickup);
        points.addAll(getOff);
        points.addAll(deliver);
    }

    @Override
    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can not parse input as string");
        }
    }
}
