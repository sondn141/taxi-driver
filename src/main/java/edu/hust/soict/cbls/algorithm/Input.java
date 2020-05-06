package edu.hust.soict.cbls.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hust.soict.cbls.common.datastructure.*;

import java.util.ArrayList;
import java.util.List;

public class Input {

    private List<Passenger> passengers;
    private List<GetOff> getOffs;
    private List<Commodity> commodities;
    private List<Deliver> delivers;
    private List<Taxi> taxis;

    private List<Point> points;

    private double[][] distanceMat;

    public Point getPassenger(int index){
        return passengers.get(index);
    }

    public Point getCommodity(int index){
        return commodities.get(index);
    }

    public Point getTaxi(int index){
        return taxis.get(index);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public List<Taxi> getTaxis() {
        return taxis;
    }

    public double[][] getDistanceMat() {
        return this.distanceMat;
    }

    public void addPassenger(Passenger passenger){
        this.passengers.add(passenger);
    }

    public void addCommodity(Commodity commodity){
        this.commodities.add(commodity);
    }

    public void addTaxi(Taxi taxi){
        this.taxis.add(taxi);
    }

    public double[][] createDistanceMatrix(){
        List<Point> points = new ArrayList<>();
        points.addAll(passengers);
        points.addAll(commodities);
        points.addAll(getOffs);
        points.addAll(delivers);
        points.addAll(taxis);
        int n = points.size();
        for(int i = 0 ; i < n - 1 ; i ++){
            for(int j = i + 1 ; j < n ; j ++){
                distanceMat[i][j] = distanceMat[j][i] = points.get(i).distance(points.get(j));
            }
        }

        return distanceMat;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

    public void setTaxis(List<Taxi> taxis) {
        this.taxis = taxis;
    }

    public List<GetOff> getGetOff() {
        return getOffs;
    }

    public void setGetOff(List<GetOff> getOff) {
        this.getOffs = getOff;
    }

    public List<Deliver> getDeliver() {
        return delivers;
    }

    public void setDeliver(List<Deliver> deliver) {
        this.delivers = deliver;
    }

    public Point getPoint(int index){
        if(points == null || points.isEmpty()){
            points = new ArrayList<>();
            points.addAll(passengers);
            points.addAll(commodities);
            points.addAll(getOffs);
            points.addAll(delivers);
            points.addAll(taxis);
        }

        return points.get(index);
    }

    public List<Point> getAllPoints(){
        if(points == null || points.isEmpty()){
            points = new ArrayList<>();
            points.addAll(passengers);
            points.addAll(commodities);
            points.addAll(getOffs);
            points.addAll(delivers);
            points.addAll(taxis);
        }

        return points;
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
