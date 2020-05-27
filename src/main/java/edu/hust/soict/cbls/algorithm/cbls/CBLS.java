package edu.hust.soict.cbls.algorithm.cbls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.algorithm.cbls.constraints.CPickupDeliveryOfGoodVR;
import edu.hust.soict.cbls.algorithm.cbls.constraints.CPickupDeliveryOfPeopleVR;
import edu.hust.soict.cbls.algorithm.cbls.neighborhoodexploration.GoodsMoveExplorer;
import edu.hust.soict.cbls.algorithm.cbls.neighborhoodexploration.PeopleMoveExplorer;
import edu.hust.soict.cbls.algorithm.impl.MySolution;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.datastructure.Pair;
import edu.hust.soict.cbls.data.Reader;
import localsearch.domainspecific.vehiclerouting.vrp.*;
import localsearch.domainspecific.vehiclerouting.vrp.constraints.leq.Leq;
import localsearch.domainspecific.vehiclerouting.vrp.entities.ArcWeightsManager;
import localsearch.domainspecific.vehiclerouting.vrp.entities.NodeWeightsManager;
import localsearch.domainspecific.vehiclerouting.vrp.entities.Point;
//import localsearch.domainspecific.vehiclerouting.vrp.entities.*;
import localsearch.domainspecific.vehiclerouting.vrp.functions.*;
import localsearch.domainspecific.vehiclerouting.vrp.invariants.*;
import localsearch.domainspecific.vehiclerouting.vrp.neighborhoodexploration.INeighborhoodExplorer;

public class CBLS extends Solver {
//	public static int PEOPLE = 1;
//	public static int GOOD = 0;
//	int scale = 100000;
	private ArrayList<Point> points;
//	public ArrayList<Point> pickupPoints;
//	public ArrayList<Point> deliveryPoints;
	
//	ArrayList<Integer> type;
//	ArrayList<Point> startPoints;
//	ArrayList<Point> stopPoints;
//	
//	public ArrayList<Point> rejectPoints;
//	public ArrayList<Point> rejectPickupGoods;
//	public ArrayList<Point> rejectPickupPeoples;
	private HashMap<Point,Point> pickup2DeliveryOfGood;
	private HashMap<Point,Point> pickup2DeliveryOfPeople;
//	public HashMap<Point, Point> pickup2Delivery;
//	public HashMap<Point,Point> delivery2Pickup;
	
	private HashMap<Point, AccumulatedNodeWeightsOnPathVR> weights;
	private Info info;
	public int K, N, M;
	public static double MAX_DISTANCE;
	
	ArcWeightsManager awm;
	NodeWeightsManager nwm;
	
	AccumulatedWeightNodesVR awn;
	AccumulatedWeightEdgesVR awe;
	
	VRManager mgr;
	VarRoutesVR XR;
	ConstraintSystemVR S;
	public IFunctionVR objective;
	LexMultiFunctions F;
	
//	AccumulatedWeightEdgesVR accDisInvr;
//	HashMap<Point, IFunctionVR> accDisF;
	private IFunctionVR[] costRoute;
	
//	public Model(Properties props) {
//		super(props);
//		this.info = new Info(input);
//		points = info.allPoints();
//	}
	
	public CBLS(Properties props) {
		super(props);
		this.info = new Info(input);
		points = info.allPoints();
	}
	
	private void stateModel() {
		mgr = new VRManager();
		XR = new VarRoutesVR(mgr);
		S = new ConstraintSystemVR(mgr);
		
		N = info.N;
		M = info.M;
		K = info.K;
		
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

		awn = new AccumulatedWeightNodesVR(XR, nwm);
		for (Point p: info.goodPickupPoints()) {
			AccumulatedNodeWeightsOnPathVR anwopvp = new AccumulatedNodeWeightsOnPathVR(awn, p);
			S.post(new Leq(0, anwopvp));
//			anwopvp.get
		}
		
		// create route length constraints
		awm = new ArcWeightsManager(points);
		for(Point px: points)
			for(Point py: points)
				awm.setWeight(px, py, info.distances[px.getID()][py.getID()]);
		awe = new AccumulatedWeightEdgesVR(XR, awm);
		costRoute = new IFunctionVR[K];

		for (int k=0; k<K; k++)
			costRoute[k] = new AccumulatedEdgeWeightsOnPathVR(awe, XR.endPoint(k+1));
		
		// add objective(s)
		objective = new MaxVR(costRoute);
		F = new LexMultiFunctions();
		F.add(S);
		F.add(objective);
		
		mgr.close();
	}
	
//	private void randomSolution() {
//		Random r = new Random();
//		List<Point> peoplePickup = new ArrayList<>(pickup2DeliveryOfPeople.keySet());
//		Collections.shuffle(peoplePickup);
//		for (Point pickup: peoplePickup) {
//			Point delivery = pickup2DeliveryOfPeople.get(pickup);
//			Point pos = XR.startPoint(r.nextInt(K) + 1);
//			mgr.performAddTwoPoints(pickup, pos, delivery, pos);
//		}
//		
//		List<Point> goodsPickup = new ArrayList<>(pickup2DeliveryOfGood.keySet());
//		Collections.shuffle(goodsPickup);
//		for (Point pickup: goodsPickup) {
//			Point delivery = pickup2DeliveryOfGood.get(pickup);
//			
//			ArrayList<Pair<Point, Point>> positions = new ArrayList<>();
//			for (int k=1; k<=K; k++)
//				for (Point p = XR.startPoint(k); p != XR.endPoint(k); p = XR.next(p))
//					for (Point q = p; q != XR.endPoint(k); q = XR.next(q)) {
//						if (S.evaluateAddTwoPoints(pickup, p, delivery, q) > 0)
//							continue;
//						positions.add(new Pair<>(p, q));
//					}
//			Pair<Point, Point> pos = positions.get(r.nextInt(positions.size()));
//			mgr.performAddTwoPoints(pickup, pos.getK(), delivery, pos.getV());
//		}
//	}
	
	private List<List<Integer>> convertSolution() {
		List<List<Integer>> solution = new ArrayList<>();
		for (int k=1; k<=K; k++) {
			ArrayList<Integer> route = new ArrayList<>();
			
			route.add(0);
			for(Point p = XR.next(XR.startPoint(k)); p != XR.endPoint(k); p = XR.next(p))
				route.add(p.getID());
			route.add(0);
			solution.add(route);
		}
		return solution;
	}
	
	public void search(int timeLimit){
		ArrayList<INeighborhoodExplorer> NE = new ArrayList<INeighborhoodExplorer>();
		NE.add(new PeopleMoveExplorer(XR, F, pickup2DeliveryOfPeople));
		NE.add(new GoodsMoveExplorer(XR, F, pickup2DeliveryOfGood));

		MySearch se = new MySearch(mgr, S, objective,
				pickup2DeliveryOfPeople,
				pickup2DeliveryOfGood);
		se.setNeighborhoodExplorer(NE);
		se.setObjectiveFunction(F);
		se.setMaxStable(50);

		se.search(10, timeLimit);
	}

//	@Override
	public Solution solve() {
		search(10);

//		for (int k=1; k<=K; k++) {
//			System.out.print("Route " + k + ":");
//			for(Point p = XR.startPoint(k); p != XR.endPoint(k); p = XR.next(p))
//				System.out.print(" " + p.getID());
//			System.out.println();
//		}

		MySolution solution = new MySolution();
		solution.setSolution(convertSolution());
		solution.setScore(objective.getValue());
		return solution;
	}
	
//	public static void main(String[] args) throws IOException {
//		CBLS cbls = new CBLS(new Info(Reader.read("data/small.txt")));
//		cbls.stateModel();
//		cbls.solve();
//	}
}
