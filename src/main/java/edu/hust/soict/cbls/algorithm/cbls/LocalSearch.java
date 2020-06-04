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
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.datastructure.Pair;
import edu.hust.soict.cbls.common.utils.SolutionUtils;
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

public class LocalSearch extends Solver {
	private ArrayList<Point> points;
	private HashMap<Point,Point> pickup2DeliveryOfGood;
	private HashMap<Point,Point> pickup2DeliveryOfPeople;

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
	
	public LocalSearch(Properties props) {
		super(props);
		this.info = new Info(input);
		points = info.allPoints();
	}
	
	protected void stateModel() {
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
	
	protected List<List<Integer>> convertSolution() {
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
	
	public void search(int maxStable, int maxIter, int timeLimit){
		ArrayList<INeighborhoodExplorer> NE = new ArrayList<INeighborhoodExplorer>();
		NE.add(new PeopleMoveExplorer(XR, F, pickup2DeliveryOfPeople));
		NE.add(new GoodsMoveExplorer(XR, F, pickup2DeliveryOfGood));

		VRPPDSearch se = new VRPPDSearch(mgr, S, costRoute,
				pickup2DeliveryOfPeople,
				pickup2DeliveryOfGood);
		se.adaptNeighborhood = false;
		se.setNeighborhoodExplorer(NE);
		se.setObjectiveFunction(F);
		se.setMaxStable(maxStable);

		se.search(maxIter, timeLimit);
	}

//	@Override
	public Solution solve() {
		stateModel();
		search(props.getIntProperty(Const.LS_MAX_STABLE, 50),
				props.getIntProperty(Const.LS_MAX_ITER, 100),
				props.getIntProperty(Const.LS_TIME_LIMIT, 100));

		MySolution solution = new MySolution();
		solution.setSolution(convertSolution());
		solution.setScore(objective.getValue());
//		System.out.println(SolutionUtils.validateSolution(solution.getSolution(), input));
		return solution;
	}
	
//	public static void main(String[] args) throws IOException {
//		CBLS cbls = new CBLS(new Info(Reader.read("data/small.txt")));
//		cbls.stateModel();
//		cbls.solve();
//	}
}
