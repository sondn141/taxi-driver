package edu.hust.soict.cbls.common.utils;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.entity.Commodity;

import java.util.List;

public class SolutionUtils {

    public static double evaluate(Input input, List<List<Integer>> routes){
        double longest = -1.0;
        double[][] distanceMat = input.getDistanceMat();
        for(List<Integer> route : routes){
            double dis = distanceMat[0][route.get(0)];
            for(int i = 1 ; i < route.size() ; i ++){
                dis += distanceMat[route.get(i - 1)][route.get(i)];
            }
            dis += distanceMat[route.get(route.size() - 1)][0];
            longest = Math.max(dis, longest);
        }

        return longest;
    }

    public static int firstPossiblePickupIndex(Input input, Commodity commodity, List<Integer> route, double cap){
        if(route.isEmpty())
            return commodity.getWeight() > cap ? -1 : 0;
        double rem = cap;
        for(int i = 0 ; i < route.size() ; i ++){
            int pointIdx = route.get(i);
            int type = input.pointType(pointIdx);
            if(type == 2 || type == 4){
                Commodity c = input.getCommodity(pointIdx);
                rem += type == 2 ? c.getWeight() : -c.getWeight();
                if(rem >= commodity.getWeight())
                    return i + 1;
            }
        }

        return route.size();
    }

}
