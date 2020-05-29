package edu.hust.soict.cbls.common.utils;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Point;

import java.util.LinkedList;
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

    public static int possiblePickupIndex(Input input, Commodity commodity, List<Integer> route, double cap){
        if(route.isEmpty())
            return commodity.getWeight() > cap ? -1 : 0;
        double rem = cap;
        P:
        for(int i = 0 ; i < route.size() ; i ++){
            int pointIdx = route.get(i);
            int type = input.pointType(pointIdx);
            if(type == 2 || type == 4){
                Commodity c = input.getCommodity(pointIdx);
                rem += type == 2 ? -c.getWeight() : c.getWeight();
                if(rem >= commodity.getWeight()){
                    double tmp = rem - commodity.getWeight();
                    for(int j = i + 1 ; j < route.size() ; j ++){
                        int p = route.get(j);
                        int t = input.pointType(p);
                        tmp += t == 4 ? input.getCommodity(p).getWeight() : (t == 2 ? -input.getCommodity(p).getWeight() : 0.0);
                        if(tmp < 0 || tmp > cap)
                            continue P;
                    }

                    List<Integer> lol = new LinkedList<>(route);
                    lol.add(i + 1, commodity.getPickup().getIdx());
                    if(!SolutionUtils.validateSingleRouteCap(lol, input, cap)){
                        System.out.println();
                    }
                    return i + 1;
                }
            }
        }

        return commodity.getWeight() > cap ? -1 : route.size();
    }

    public static boolean validateRouteAllDiff(List<List<Integer>> routes){
        int n = routes.stream().mapToInt(List::size).sum();
        boolean[] appr = new boolean[n + 1];

        for(List<Integer> r : routes)
            for(Integer i : r)
                if(i != 0 && appr[i])
                    return false;
                else
                    appr[i] = true;
        return true;
    }

    public static boolean validateSingleRouteCap(List<Integer> route, Input inp, double cap){
        double rem = cap;
        for(Integer e : route){
            int type = inp.pointType(e);
            if(type == 2){
                double w =inp.getCommodity(e).getWeight();
                rem -= w;
            }
            else if(type == 4)
                rem += inp.getCommodity(e).getWeight();
            if(rem < -Const.EPSI || rem > cap + Const.EPSI)
                return false;
        }

        return true;
    }

    /**
     * @author hungth
     * @since 20200528
     * */
    public static void validateSolution(List<List<Integer>> routes, Input inp) {
    	int N = inp.getPassengers().size();
    	int M = inp.getCommodities().size();
    	int K = inp.getTaxis().size();
    	
    	int[] indices = new int[2*N*2*M+1];
    	int[] r = new int[2*N*2*M+1];
    	double[] w = new double[2*N*2*M+1];
//    	double score = 0;
    	
    	inp.getDistanceMat();
    	
    	List<Point> points = inp.getPoints();
    	
    	int k = 0;
    	for (List<Integer> route: routes) {
    		double cap = inp.getTaxis().get(k++).getCap();
    		if (route.get(0) != 0 || route.get(route.size() - 1) != 0) {
    			throw new RuntimeException("Incorrect station!");
    		}
    		for (int i=1; i<route.size()-1; i++) {
    			int p = route.get(i);
    			int prev = route.get(i-1);
    			r[p] = k;
    			indices[p] = indices[prev] + 1;
    			if (inp.pointType(p) == Input.COMMODITY_PICKUP)
    				w[p] = w[prev] + inp.getCommodities().get(p-N-1).getWeight();
    			else if (inp.pointType(p) == Input.COMMODITY_DELIVER)
    				w[p] = w[prev] - inp.getCommodities().get(p-2*N-M-1).getWeight();
    			else w[p] = w[prev];
    			if (w[p] < -Const.EPSI || w[p] > cap + Const.EPSI) {
    			    throw new RuntimeException("Capacity violated");
    			}
//    			score += inp.distance(prev, p);
//    			score += points.get(prev).distance(points.get(p));
    		}
//    		score += inp.distance(route.get(route.size()-2), 0);
//    		score += points.get(route.get(route.size()-2)).distance(points.get(0));
    	}
    	for (int i=1; i<=N; i++)
    		if (r[i] != r[i+N+M] || indices[i] + 1 != indices[i+N+M]) {
    			throw new RuntimeException("Passenger order failed!");
    		}
    	for (int i=N+1; i<=N+M; i++)
    		if (r[i] != r[i+N+M] || indices[i] >= indices[i+N+M]) {
    			throw new RuntimeException("Commodity order failed!");
    		}
    }
}
