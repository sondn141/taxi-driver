package edu.hust.soict.cbls.algorithm.cbls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import edu.hust.soict.cbls.common.datastructure.Pair;
import localsearch.domainspecific.vehiclerouting.vrp.ConstraintSystemVR;
import localsearch.domainspecific.vehiclerouting.vrp.IFunctionVR;
import localsearch.domainspecific.vehiclerouting.vrp.VRManager;
import localsearch.domainspecific.vehiclerouting.vrp.entities.Point;
import localsearch.domainspecific.vehiclerouting.vrp.functions.LexMultiFunctions;
import localsearch.domainspecific.vehiclerouting.vrp.search.GenericLocalSearch;

public class VRPPDSearch extends GenericLocalSearch{
	public ConstraintSystemVR S;
//	public IFunctionVR objective;
	
	private HashMap<Point,Point> pickup2DeliveryOfGood;
	private HashMap<Point,Point> pickup2DeliveryOfPeople;
	private IFunctionVR[] costRoute;
	
	private Random r = new Random();
	
	private List<Point> peopleKey;
	private List<Point> goodKey;
	
	public VRPPDSearch(VRManager mgr, ConstraintSystemVR S, IFunctionVR[] costRoute,
			HashMap<Point,Point> pickup2DeliveryOfPeople,
			HashMap<Point,Point> pickup2DeliveryOfGood){
		super(mgr);
		this.S = S;
		this.costRoute = costRoute;

		this.pickup2DeliveryOfPeople = pickup2DeliveryOfPeople;
		this.pickup2DeliveryOfGood = pickup2DeliveryOfGood;
		peopleKey = new ArrayList<>(pickup2DeliveryOfPeople.keySet());
		goodKey = new ArrayList<>(pickup2DeliveryOfGood.keySet());
	}
	
	public String name(){
		return "VRPPDSearch";
	}
	@Override
	public void generateInitialSolution(){
		System.out.println(name() + "::generateInitialSolution.....");
		greedyInit();
	}
	
	private void randomInit() {
		List<Point> peoplePickup = new ArrayList<>(pickup2DeliveryOfPeople.keySet());
		Collections.shuffle(peoplePickup);
		for (Point pickup: peoplePickup) {
			Point delivery = pickup2DeliveryOfPeople.get(pickup);
			Point pos = XR.startPoint(r.nextInt(XR.getNbRoutes()) + 1);
			mgr.performAddTwoPoints(pickup, pos, delivery, pos);
		}
		
		List<Point> goodsPickup = new ArrayList<>(pickup2DeliveryOfGood.keySet());
		Collections.shuffle(goodsPickup);
		for (Point pickup: goodsPickup) {
			Point delivery = pickup2DeliveryOfGood.get(pickup);
			
			ArrayList<Pair<Point, Point>> positions = new ArrayList<>();
			for (int k=1; k<=XR.getNbRoutes(); k++)
				for (Point p = XR.startPoint(k); p != XR.endPoint(k); p = XR.next(p)) {
					if (S.evaluateAddTwoPoints(pickup, p, delivery, p) > 0)
						break;
					for (Point q = p; q != XR.endPoint(k); q = XR.next(q))
						if (S.evaluateAddTwoPoints(pickup, p, delivery, q) <= 0)
							positions.add(new Pair<>(p, q));
				}
			Pair<Point, Point> pos = positions.get(r.nextInt(positions.size()));
			mgr.performAddTwoPoints(pickup, pos.getK(), delivery, pos.getV());
		}
	}
	
	private void greedyInit() {
		for (Point x1: goodKey) {
			Point x2 = pickup2DeliveryOfGood.get(x1);
			
			double minCost = Double.MAX_VALUE;
			
			ArrayList<Pair<Point, Point>> positions = new ArrayList<>();
			for (int k=1; k<=XR.getNbRoutes(); k++) {
				IFunctionVR cost = costRoute[k-1];
				for (Point p = XR.startPoint(k); p != XR.endPoint(k); p = XR.next(p)) {
					if (S.evaluateAddTwoPoints(x1, p, x2, p) > 0)
						break;
					for (Point q = p; q != XR.endPoint(k); q = XR.next(q)) {
						if (S.evaluateAddTwoPoints(x1, p, x2, q) > 0)
							break;
						double newCost = cost.getValue() + cost.evaluateAddTwoPoints(x1, p, x2, q);
						if (newCost < minCost) {
							minCost = newCost;
							positions.clear();
							positions.add(new Pair<>(p, q));
						}
						else if (newCost == minCost)
							positions.add(new Pair<>(p, q));
					}
				}
			}
			Pair<Point, Point> y = positions.get(r.nextInt(positions.size()));
			mgr.performAddTwoPoints(x1, y.getK(), x2, y.getV());
		}
		
		for (Point x1: peopleKey) {
			Point x2 = pickup2DeliveryOfPeople.get(x1);
			
			double minCost = Double.MAX_VALUE;
			
			ArrayList<Point> positions = new ArrayList<>();
			for (int k=1; k<=XR.getNbRoutes(); k++) {
				IFunctionVR cost = costRoute[k-1];
				for (Point p = XR.startPoint(k); p != XR.endPoint(k); p = XR.next(p)) {
					if (S.evaluateAddTwoPoints(x1, p, x2, p) > 0)
						continue;
					double newCost = cost.getValue() + cost.evaluateAddTwoPoints(x1, p, x2, p);
					if (newCost < minCost) {
						minCost = newCost;
						positions.clear();
						positions.add(p);
					}
					else if (newCost == minCost)
						positions.add(p);
				}
			}
			Point y = positions.get(r.nextInt(positions.size()));
			mgr.performAddTwoPoints(x1, y, x2, y);
		}
	}
	
	@Override
	public void perturb(int nbSteps){
		ArrayList<Point> P = XR.getClientPoints();
		for(int k = 1; k <= nbSteps; k++){
//			ArrayList<Point> P = XR.collectClientPointsOnRoutes();
//			if(P.size() >= 2){
//				for(int i = 1; i <= 10; i++){
//					Point x = P.get(R.nextInt(P.size()));
//					
//					Point y = P.get(R.nextInt(P.size()));
//					
//					if(x != y && XR.checkPerformOnePointMove(x, y)){
//						mgr.performOnePointMove(x, y);
//						break;
//					}
//					
//				}
//			}
			if (r.nextDouble() < 0.5) {
				for (int i=1; i<=10; i++) {
					Point x1 = peopleKey.get(r.nextInt(peopleKey.size()));
					Point x2 = pickup2DeliveryOfPeople.get(x1);
					
					Point y = P.get(r.nextInt(P.size()));
					if (XR.checkPerformTwoPointsMove(x1, x2, y, y)) {
						mgr.performTwoPointsMove(x1, x2, y, y);
						break;
					}
				}
			}
			else {
				for (int i=1; i<=10; i++) {
					Point x1 = goodKey.get(r.nextInt(goodKey.size()));
					Point x2 = pickup2DeliveryOfGood.get(x1);
					
					int id = r.nextInt(XR.getNbRoutes()) + 1;
					ArrayList<Point> clientsOnRoute = new ArrayList<>();
					for (Point p=XR.startPoint(id); p != XR.endPoint(id); p=XR.next(p))
						clientsOnRoute.add(p);
					Point y1 = clientsOnRoute.get(r.nextInt(clientsOnRoute.size()));
					Point y2 = clientsOnRoute.get(r.nextInt(clientsOnRoute.size()));
					if (XR.checkPerformTwoPointsMove(x1, x2, y1, y2)) {
						mgr.performTwoPointsMove(x1, x2, y1, y2);
						break;
					}
				}
			}
		}
	}
}
