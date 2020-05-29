package edu.hust.soict.cbls.data;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Passenger;
import edu.hust.soict.cbls.entity.Point;
import edu.hust.soict.cbls.entity.Taxi;
import edu.hust.soict.cbls.common.utils.RandomUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class Generate {

    private int N = 5;
    private int M = 7;
    private int K = 2;

    private Distribution pLocation = Distribution.UNIFORM;
    private Distribution cLocation = Distribution.UNIFORM;
    private Distribution sLocation = Distribution.UNIFORM;
    private double locationMean = 500.0;
    private double locationStd = 50.0;
    private double locationMax = 1000.0;
    private double locationMin = 0.0;
    private int numGrid = 10;

    private Distribution capDistribution = Distribution.UNIFORM;
    private double capMean = 100.0;
    private double capStd = 10.0;
    private double capMin = 10.0;
    private double capMax = 200.0;

    private Distribution weiDistribution = Distribution.UNIFORM;
    private double weiMean = 100.0;
    private double weiStd = 10.0;
    private double weiMin = 10.0;
    private double weiMax = 200.0;

    private Input input;

    public Generate setNumOfPassengers(int n){
        this.N = n;
        return this;
    }

    public Generate setNumOfCommodities(int m){
        this.M = m;
        return this;
    }

    public Generate setNumOfTaxi(int k){
        this.K = k;
        return this;
    }

    public void setCapDistribution(Distribution capDistribution) {
        this.capDistribution = capDistribution;
    }

    public void setWeiDistribution(Distribution weiDistribution) {
        this.weiDistribution = weiDistribution;
    }

    public Generate setPassengerLocationDistribution(Distribution pLocation){
        this.pLocation = pLocation;
        return this;
    }

    public Generate setCommodityLocationDistribution(Distribution cLocation){
        this.cLocation = cLocation;
        return this;
    }

    public Generate setStationLocationDistribution(Distribution sLocation){
        this.sLocation = sLocation;
        return this;
    }

    public Generate generate(){
        this.input = new Input();

        List<Point> passengerPoints = getPoints(2 * N);
        List<Passenger> passengers = new LinkedList<>();
        for(int i = 0 ;i < N ; i ++){
            passengers.add(new Passenger(passengerPoints.get(i), passengerPoints.get(i + N)));
        }

        List<Point> commodityPoints = getPoints(2 * M);
        List<Double> weis = getWei();
        List<Commodity> commodities = new LinkedList<>();
        for(int i = 0 ; i < M ; i ++){
            commodities.add(new Commodity(commodityPoints.get(i), commodityPoints.get(i + M), weis.get(i)));
        }

        Point station = RandomUtils.PointUtils.singlePoint(locationMin, locationMax);
        List<Double> caps = getCap();
        Taxi.setStation(station);
        List<Taxi> taxis = new LinkedList<>();
        for(int i = 0 ; i < K ; i ++){
            taxis.add(new Taxi(caps.get(i)));
        }

        this.input.setPassengers(passengers);
        this.input.setCommodities(commodities);
        this.input.setTaxies(taxis);

        validate(input);
        return this;
    }

    private List<Double> getCap(){
        switch (capDistribution){
            case NORMAL: {
                return RandomUtils.randNormals(K, capMean, capStd, capMin, capMax);
            }

            case UNIFORM:{
                return RandomUtils.randUniforms(K, capMin, capMax);
            }

            default:
                throw new UnsupportedOperationException();
        }
    }

    private List<Double> getWei(){
        switch (weiDistribution){
            case NORMAL: {
                return RandomUtils.randNormals(M, weiMean, weiStd, weiMin, weiMax);
            }

            case UNIFORM:{
                return RandomUtils.randUniforms(M, weiMin, weiMax);
            }

            default:
                throw new UnsupportedOperationException();
        }
    }

    private List<Point> getPoints(int n){
        switch (pLocation){
            case NORMAL: {
                return RandomUtils.PointUtils
                        .normalPoints(n, locationMean, locationStd, locationMin, locationMax);
            }

            case UNIFORM:{
                return RandomUtils.PointUtils
                        .uniformPoints(n, locationMin, locationMax);
            }

            case GRID:{
                return RandomUtils.PointUtils
                        .gridPoints(n, numGrid, locationMin, locationMax);
            }

            default:
                throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Input validate(Input input){
        double maxW = input.getCommodities().stream().mapToDouble(Commodity::getWeight).max().getAsDouble();
        double maxC = input.getTaxis().stream().mapToDouble(Taxi::getCap).max().getAsDouble();

        if(maxC < maxW){
            double newW = RandomUtils.rd.nextDouble() * maxC + maxW;
            int chosenTaxi = RandomUtils.rd.nextInt(input.getTaxis().size());

            input.setTaxi(new Taxi(newW), chosenTaxi);
        }
        return input;
    }

    public void save(String path){
        if(input == null)
            throw new RuntimeException("Data initialization failure");

        Writer.write(input, path);
    }

    public enum Distribution{
        NORMAL("n"),
        UNIFORM("u"),
        GRID("g");

        String label;

        Distribution(String value){
            this.label = value;
        }
    }

}
