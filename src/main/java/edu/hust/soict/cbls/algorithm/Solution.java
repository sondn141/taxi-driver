package edu.hust.soict.cbls.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<List<Integer>> routes;
    private Double score;

    public Solution(int k){
        this.routes = new ArrayList<>();
        for(int i = 0 ; i < k ; i ++){
            routes.add(null);
        }
    }

    public void setRoute(List<Integer> route, int idx){
        if(idx < routes.size())
            this.routes.set(idx, route);
        else throw new IndexOutOfBoundsException("K = " + routes.size() + " while index = " + idx);
    }

    public List<List<Integer>> getRoute(){
        return this.routes;
    }

    public double getScore(){
        return this.score;
    }

    public void evaluate(Input input){
        double[][] distanceMat = input.getDistanceMat();
        double score = 0.0;
        for(List<Integer> route : routes){
            for(int i = 1 ; i < route.size() ; i ++){

            }
        }
        this.score = 0.0;
    }

}
