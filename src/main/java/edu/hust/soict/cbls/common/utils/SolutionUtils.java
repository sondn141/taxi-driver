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
            if(route.isEmpty())
                continue;
            double dis = route.get(0) == 0 ? 0.0 : distanceMat[0][route.get(0)];
            for(int i = 1 ; i < route.size() ; i ++){
                dis += distanceMat[route.get(i - 1)][route.get(i)];
            }
            dis += route.get(route.size() - 1) == 0 ? 0.0 : distanceMat[route.get(route.size() - 1)][0];
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

    public static boolean validateRouteAllDiff(List<List<Integer>> routes){
        int n = routes.stream().mapToInt(List::size).sum();
        boolean[] appr = new boolean[n + 1];

        for(List<Integer> r : routes)
            for(Integer i : r)
                if(appr[i])
                    return false;
                else
                    appr[i] = true;
        return true;
    }

    public static boolean validatePassengerGetIn(List<List<Integer>> routes, Input inp){
        for(List<Integer> route : routes){
            for(int i = 0 ; i < route.size() ; i ++){
                int type = inp.pointType(route.get(i));
                if(type == 1){
                    int type2 = inp.pointType(route.get(i + 1));
                    if(!(type2 == 3 && inp.getPassengerGetOff(route.get(i)) == route.get(i + 1))){
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
