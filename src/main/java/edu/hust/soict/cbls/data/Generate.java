package edu.hust.soict.cbls.data;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.datastructure.*;
import edu.hust.soict.cbls.common.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Generate {

    private int N = 5;
    private int M = 7;
    private int K = 2;

    private Distribution pLocation = Distribution.UNIFORM;
    private Distribution cLocation = Distribution.UNIFORM;
    private Distribution sLocation = Distribution.UNIFORM;

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

//    public int getNumOfPassengers(int n){
//        return this.N;
//    }
//
//    public int getNumOfCommodities(int m){
//        return this.M;
//    }
//
//    public int getNumOfTaxi(int k){
//        return this.K;
//    }
//
//    public Distribution getPassengerLocationDistribution(){
//        return this.pLocation;
//    }
//
//    public Distribution getCommodityLocationDistribution(){
//        return this.cLocation;
//    }
//
//    public Distribution getStationLocationDistribution(){
//        return this.sLocation;
//    }

    public Generate generate(){
        this.input = new Input();
        input.setPassengers(genPassengers());
        input.setGetOff(genGetOff());
        input.setCommodities(genCommodities());
        input.setDeliver(genDeliver());
        input.setTaxis(genTaxies());

        validate(input);
        return this;
    }
    private List<Passenger> genPassengers(){
        List<Passenger> passengers = new ArrayList<>();
        switch (pLocation){
            case NORMAL: {
                for(int i = 0 ; i < N ; i ++){

                }

                break;
            }

            case UNIFORM:{
                for(int i = 0 ; i < N ; i ++){
                    passengers.add(
                            new Passenger(RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(0.0, 1000.0)));
                }

                break;
            }

            case GRID:{
                for(int i = 0 ; i < N ; i ++){

                }

                break;
            }

            default:
                throw new UnsupportedOperationException();
        }

        return passengers;
    }
    private List<GetOff> genGetOff(){
        List<GetOff> getOff = new ArrayList<>();
        switch (pLocation){
            case NORMAL: {
                for(int i = 0 ; i < N ; i ++){

                }

                break;
            }

            case UNIFORM:{
                for(int i = 0 ; i < N ; i ++){
                    getOff.add(
                            new GetOff(RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(0.0, 1000.0)));
                }

                break;
            }

            case GRID:{
                for(int i = 0 ; i < N ; i ++){

                }

                break;
            }

            default:
                throw new UnsupportedOperationException();
        }

        return getOff;
    }
    private List<Commodity> genCommodities(){
        List<Commodity> commodities = new ArrayList<>();
        switch (pLocation){
            case NORMAL: {
                for(int i = 0 ; i < M ; i ++){

                }

                break;
            }

            case UNIFORM:{
                for(int i = 0 ; i < M ; i ++){
                    commodities.add(
                            new Commodity(RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(10.0, 100.0)));
                }

                break;
            }

            case GRID:{
                for(int i = 0 ; i < M ; i ++){

                }

                break;
            }

            default:
                throw new UnsupportedOperationException();
        }

        return commodities;
    }
    private List<Deliver> genDeliver(){
        List<Deliver> deliver = new ArrayList<>();
        switch (pLocation){
            case NORMAL: {
                for(int i = 0 ; i < M ; i ++){

                }

                break;
            }

            case UNIFORM:{
                for(int i = 0 ; i < M ; i ++){
                    deliver.add(
                            new Deliver(RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(0.0, 1000.0)));
                }

                break;
            }

            case GRID:{
                for(int i = 0 ; i < M ; i ++){

                }

                break;
            }

            default:
                throw new UnsupportedOperationException();
        }

        return deliver;
    }
    private List<Taxi> genTaxies(){
        List<Taxi> taxies = new ArrayList<>();
        switch (pLocation){
            case NORMAL: {
                for(int i = 0 ; i < K ; i ++){

                }

                break;
            }

            case UNIFORM:{
                for(int i = 0 ; i < K ; i ++){
                    taxies.add(
                            new Taxi(RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(0.0, 1000.0), RandomUtils.randUniform(10.0, 100.0)));
                }

                break;
            }

            case GRID:{
                for(int i = 0 ; i < K ; i ++){

                }

                break;
            }

            default:
                throw new UnsupportedOperationException();
        }

        return taxies;
    }

    private Input validate(Input input){

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
