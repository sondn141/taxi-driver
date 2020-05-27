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
import localsearch.domainspecific.vehiclerouting.vrp.search.GenericLocalSearch;

public class MySearch extends GenericLocalSearch{
	public ConstraintSystemVR S;
	public IFunctionVR objective;
	private HashMap<Point,Point> pickup2DeliveryOfGood;
	private HashMap<Point,Point> pickup2DeliveryOfPeople;
	
	public MySearch(VRManager mgr, ConstraintSystemVR S, IFunctionVR objective,
			HashMap<Point,Point> pickup2DeliveryOfPeople,
			HashMap<Point,Point> pickup2DeliveryOfGood){
		super(mgr);
		this.S = S;
		this.objective = objective;
		this.pickup2DeliveryOfPeople = pickup2DeliveryOfPeople;
		this.pickup2DeliveryOfGood = pickup2DeliveryOfGood;
	}
	public String name(){
		return "VRPPD Search";
	}
	@Override
	public void generateInitialSolution(){
		System.out.println(name() + "::generateInitialSolution.....");
		Random r = new Random();
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
}
