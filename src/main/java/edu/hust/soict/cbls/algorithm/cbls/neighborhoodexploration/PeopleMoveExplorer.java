package edu.hust.soict.cbls.algorithm.cbls.neighborhoodexploration;

import java.util.HashMap;

import localsearch.domainspecific.vehiclerouting.vrp.VRManager;
import localsearch.domainspecific.vehiclerouting.vrp.VarRoutesVR;
import localsearch.domainspecific.vehiclerouting.vrp.entities.LexMultiValues;
import localsearch.domainspecific.vehiclerouting.vrp.entities.Point;
import localsearch.domainspecific.vehiclerouting.vrp.functions.LexMultiFunctions;
import localsearch.domainspecific.vehiclerouting.vrp.moves.IVRMove;
import localsearch.domainspecific.vehiclerouting.vrp.moves.MoveTwoPointsMove;
import localsearch.domainspecific.vehiclerouting.vrp.neighborhoodexploration.INeighborhoodExplorer;
import localsearch.domainspecific.vehiclerouting.vrp.search.Neighborhood;

public class PeopleMoveExplorer implements INeighborhoodExplorer {
	private VRManager mgr;
	private VarRoutesVR XR;
	private LexMultiFunctions F;
	private boolean firstImprovement = true;
	
	private HashMap<Point,Point> pickup2DeliveryOfPeople;
	
	public PeopleMoveExplorer(VarRoutesVR XR, LexMultiFunctions F,
			HashMap<Point,Point> pickup2DeliveryOfPeople) {
		this.XR = XR;
		this.F = F;
		this.mgr = XR.getVRManager();
		this.pickup2DeliveryOfPeople = pickup2DeliveryOfPeople;
	}
	
	public PeopleMoveExplorer(VarRoutesVR XR, LexMultiFunctions F, 
			boolean firstImprovement,
			HashMap<Point,Point> pickup2DeliveryOfGood) {
		this.XR = XR;
		this.F = F;
		this.mgr = XR.getVRManager();
		this.firstImprovement = firstImprovement;
		this.pickup2DeliveryOfPeople = pickup2DeliveryOfGood;
	}
	
	@Override
	public void exploreNeighborhood(Neighborhood N, LexMultiValues bestEval) {
		if(firstImprovement && N.hasImprovement()){
			System.out.println(name() + "::exploreNeighborhood, has improvement --> RETURN");
			return;
		}
		
		for (HashMap.Entry<Point, Point> entry : pickup2DeliveryOfPeople.entrySet()) {
			Point p = entry.getKey();
			Point d = entry.getValue();
			
			for (int k=1; k<=XR.getNbRoutes(); k++)
				for (Point x = XR.startPoint(k); x != XR.endPoint(k); x = XR.next(x)) {
					if (!XR.checkPerformTwoPointsMove(p, d, x, x))
						continue;
					LexMultiValues eval = F.evaluateTwoPointsMove(p, d, x, x);
					if (eval.lt(bestEval)) {
						N.clear();
						N.add(new MoveTwoPointsMove(mgr, eval, p, d, x, x, this));
					}
					else if (eval.eq(bestEval))
						N.add(new MoveTwoPointsMove(mgr, eval, p, d, x, x, this));
					if(firstImprovement){
						if(eval.lt(0)) return;
					}
				}
		}
	}

	@Override
	public void performMove(IVRMove m) {
		// TODO Auto-generated method stub

	}

	@Override
	public String name() {
		return "PeopleMoveExplorer";
	}

}
