package edu.hust.soict.cbls.algorithm.cbls;

import java.util.ArrayList;
import java.util.HashMap;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.algorithm.cbls.constraints.CPickupDeliveryOfGoodVR;
import edu.hust.soict.cbls.algorithm.cbls.constraints.CPickupDeliveryOfPeopleVR;
import edu.hust.soict.cbls.algorithm.mip.MIP;
import edu.hust.soict.cbls.common.config.Properties;
import localsearch.domainspecific.vehiclerouting.vrp.*;
import localsearch.domainspecific.vehiclerouting.vrp.constraints.leq.Leq;
import localsearch.domainspecific.vehiclerouting.vrp.entities.*;
import localsearch.domainspecific.vehiclerouting.vrp.functions.*;
import localsearch.domainspecific.vehiclerouting.vrp.invariants.*;

public class Model extends Solver {
//	public static int PEOPLE = 1;
//	public static int GOOD = 0;
//	int scale = 100000;
	ArrayList<Point> points;
//	public ArrayList<Point> pickupPoints;
//	public ArrayList<Point> deliveryPoints;
//	ArrayList<Integer> type;
//	ArrayList<Point> startPoints;
//	ArrayList<Point> stopPoints;
//	
//	public ArrayList<Point> rejectPoints;
//	public ArrayList<Point> rejectPickupGoods;
//	public ArrayList<Point> rejectPickupPeoples;
	public HashMap<Point,Point> pickup2DeliveryOfGood;
	public HashMap<Point,Point> pickup2DeliveryOfPeople;
//	public HashMap<Point, Point> pickup2Delivery;
//	public HashMap<Point,Point> delivery2Pickup;
	private Info info;
	public int K, N, M;
	public static double MAX_DISTANCE;
	
	ArcWeightsManager awm;
	NodeWeightsManager nwm;
	VRManager mgr;
	VarRoutesVR XR;
	ConstraintSystemVR S;
	public IFunctionVR objective;
	LexMultiValues valueSolution;
	
//	AccumulatedWeightEdgesVR accDisInvr;
//	HashMap<Point, IFunctionVR> accDisF;
	private IFunctionVR[] costRoute;
	
	public Model(Properties props) {
		super(props);
		this.info = new Info(input);
		points = info.allPoints();
	}
	
	private void stateModel() {
		mgr = new VRManager();
		XR = new VarRoutesVR(mgr);
		S = new ConstraintSystemVR(mgr);
		
		// create routes
		for(int i=0;i<K;++i)
			XR.addRoute(info.startPoints().get(i), info.stopPoints().get(i));
		for (Point p: info.clientPoints())
			XR.addClientPoint(p);
		
		// create node order constraints
		pickup2DeliveryOfGood = new HashMap<Point, Point>();
		pickup2DeliveryOfPeople = new HashMap<Point, Point>();
		for (int i=1; i<=N; i++)
			pickup2DeliveryOfPeople.put(info.points.get(i), 
					info.points.get(i+N+M));
		for (int i=N+1; i<=N+M; i++)
			pickup2DeliveryOfGood.put(info.points.get(i), 
					info.points.get(i+N+M));
		IConstraintVR goodC = new CPickupDeliveryOfGoodVR(XR, pickup2DeliveryOfGood);
		IConstraintVR peopleC = new CPickupDeliveryOfPeopleVR(XR, pickup2DeliveryOfPeople);
		S.post(goodC);
		S.post(peopleC);
		
		// create weight constraints
		nwm = new NodeWeightsManager(points);
		for (Point p: points)
			nwm.setWeight(p, info.weights[p.getID()]);
		AccumulatedWeightNodesVR awn = new AccumulatedWeightNodesVR(XR, nwm);
		for (Point p: info.goodPickupPoints())
			S.post(new Leq(0, new AccumulatedNodeWeightsOnPathVR(awn, p)));
		
		// create route length constraints
		awm = new ArcWeightsManager(points);
		for(Point px: points)
			for(Point py: points)
				awm.setWeight(px, py, info.distances[px.getID()][py.getID()]);
		AccumulatedWeightEdgesVR awe = new AccumulatedWeightEdgesVR(XR, awm);
		costRoute = new IFunctionVR[K];
		for (int k=0; k<K; k++)
			costRoute[k] = new AccumulatedEdgeWeightsOnPathVR(awe, XR.endPoint(k+1));
		
		// add objective(s)
		objective = new MaxVR(costRoute);
		valueSolution = new LexMultiValues();
		valueSolution.add(S.violations());
		valueSolution.add(objective.getValue());
		
		mgr.close();
	}
	
	private void initSolution() {
		Point x = info.peoplePickupPoints().get(0);
		Point y = pickup2DeliveryOfPeople.get(x);
		mgr.performAddTwoPoints(x, XR.startPoint(1), y, XR.startPoint(1));
		for(Point p = XR.getStartingPointOfRoute(1); p != XR.getTerminatingPointOfRoute(1); p = XR.next(p))
			System.out.print(" " + p.getID());
	}

	@Override
	public Solution solve() {
		initSolution();
		return null;
	}
	
	public static void main(String[] args) {
		Model cbls = new Model(new Properties());
		cbls.solve();
	}
}
