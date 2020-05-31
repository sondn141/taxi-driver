package edu.hust.soict.cbls.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hust.soict.cbls.common.utils.CollectionUtils;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Point;
import edu.hust.soict.cbls.entity.Taxi;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Input {
	/**
     * @author hungth
     * @since 20200528
     * */
	public static final int STATION = 0;
	public static final int PASSENGER_GETIN = 1;
	public static final int COMMODITY_PICKUP = 2;
	public static final int PASSENGER_GETOFF = 3;
	public static final int COMMODITY_DELIVER = 4;

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

    public List<Double> getCap(){
        return taxis.stream().mapToDouble(Taxi::getCap).boxed().collect(Collectors.toList());
    }

    public double getCap(int index){
        if(index < taxis.size())
            return taxis.get(index).getCap();
        else throw new RuntimeException("Index passed is not a taxi index");
    }

    public int getPassengerGetOff(int index){
        int p = passengers.size();
        int c = commodities.size();

        if(index >= p + 1)
            throw new RuntimeException("Invalid passenger getting in point index");
        return index + p + c;
    }

    public Commodity getCommodity(int index){
        int p = passengers.size();
        int c = commodities.size();
        if(CollectionUtils.inRangeInclusive(1 + p, p + c, index) ||
                CollectionUtils.inRangeInclusive(p*2 + c + 1, (p + c)*2, index)){
            int idx = (index > (p + c) ? index - (p + c) : index) - 1 - p;
            return commodities.get(idx);
        } else
            throw new RuntimeException("Index passed is not a commodity index");
    }

    public int commodityPointIdx(int idx, int type){
        if(!CollectionUtils.inRangeInclusive(0, commodities.size() - 1, idx))
            throw new IndexOutOfBoundsException();
        if(type != 2 && type != 4)
            throw new InvalidParameterException("Commodity type should be 2 or 4");
        int p = passengers.size();
        int c = commodities.size();
        return 1 + p + idx + (type == 2 ? 0 : (p + c));
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
        return distanceMat[i][j];
    }

    private void createDistanceMatrix(){
        if(points == null || points.isEmpty())
            initPoints();
        if(distanceMat != null)
            return;

        int n = points.size();
        distanceMat = new double[n][n];
        for(int i = 0 ; i < n - 1 ; i ++){
//            distanceMat[i] = new double[n];
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
     * @since 20200527
     * */
    public List<Point> getPoints() {
    	if (points == null)
    		initPoints();
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
