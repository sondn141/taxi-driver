package edu.hust.soict.cbls.algorithm.cbls;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.impl.MySolution;
import edu.hust.soict.cbls.common.config.Properties;

public class Greedy extends LocalSearch {

	public Greedy(Properties props) {
		super(props);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solution solve() {
		stateModel();
		search(0, 0, 0);

		MySolution solution = new MySolution();
		solution.setSolution(convertSolution());
		solution.setScore(objective.getValue());
//		System.out.println(SolutionUtils.validateSolution(solution.getSolution(), input));
		return solution;
	}
}
