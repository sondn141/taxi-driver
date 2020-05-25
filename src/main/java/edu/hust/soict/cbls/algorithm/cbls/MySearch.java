package edu.hust.soict.cbls.algorithm.cbls;

import localsearch.domainspecific.vehiclerouting.vrp.VRManager;
import localsearch.domainspecific.vehiclerouting.vrp.search.GenericLocalSearch;

public class MySearch extends GenericLocalSearch{
	public MySearch(VRManager mgr){
		super(mgr);
	}
	public String name(){
		return "MySearch";
	}
	@Override
	public void generateInitialSolution(){
		System.out.println(name() + "::generateInitialSolution.....");
		super.generateInitialSolution();
		//System.exit(-1);
	}
}
