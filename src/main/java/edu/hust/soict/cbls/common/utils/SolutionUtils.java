package edu.hust.soict.cbls.common.utils;

import edu.hust.soict.cbls.algorithm.Input;

import java.util.List;

public class SolutionUtils {

    public static double evaluate(Input input, List<List<Integer>> routes){
        double score = 0.0;
        for(List<Integer> route : routes){
            score += input.distance(0, route.get(0)) + input.distance(route.get(route.size() - 1), 0);
            for(int i = 1 ; i < route.size() ; i ++){
                score += input.distance(route.get(i - 1), route.get(i));
            }
        }

        return score;
    }

}
