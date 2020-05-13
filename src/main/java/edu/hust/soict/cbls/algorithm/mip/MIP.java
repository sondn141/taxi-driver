package edu.hust.soict.cbls.algorithm.mip;

import java.util.ArrayList;
import java.util.List;

import com.google.ortools.linearsolver.*;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.algorithm.impl.MySolution;
import edu.hust.soict.cbls.common.config.Properties;

public class MIP extends Solver {
	static {
		System.loadLibrary("jniortools");
	}
	
	public MIP(Properties props) {
		super(props);
	}

	@Override
	public Solution solve() {
		MPSolver solver = new MPSolver(
				"MIP", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
		solver.suppressOutput();
		final int N = input.getPassengers().size();
		final int M = input.getCommodities().size();
		final int K = input.getTaxis().size();
		final double BigM = 1e6, eps = 1e-6;
		
		// w
		double[] w = new double[2*N+2*M+K];
		for (int i=1; i<=M; i++) {
			double q = input.getCommodities().get(i-1).getWeight();
			w[i+N] = q;
			w[i+2*N+M] = -q;
		}
		
		// d
		double distance[][] = input.getDistanceMat();
		double[][] d = new double[2*N+2*M+K+1][2*N+2*M+K+1];
		for (int i=1; i<=2*N+2*M+K; i++)
			for (int j=i+1; j<=2*N+2*M+K; j++)
			{
				int iID = i, jID = j;
				if (iID > 2*N+2*M) iID = 0;
				if (jID > 2*N+2*M) jID = 0;
				d[i][j] = d[j][i] = distance[iID][jID];
			}
		
		// (1)
		MPVariable[][] x = new MPVariable[2*N+2*M+K+1][2*N+2*M+K+1];
		for (int i=1; i<=2*N+2*M+K; i++)
			for (int j=1; j<=2*N+2*M+K; j++)
				x[i][j] = solver.makeBoolVar(i+"_"+j);
		
		// (2) + (3) + (4) + (7) + (9) + (11)
		MPVariable[] r = new MPVariable[2*N+2*M+K+1];
		MPVariable[] t = new MPVariable[2*N+2*M+K+1];
		MPVariable[] c = new MPVariable[2*N+2*M+K+1];
		for (int i=1; i<=2*N+2*M+K; i++) {
			if (i > 2*N+2*M) {
				int k = i-2*N-2*M;
				r[i] = solver.makeIntVar(k, k, "r"+i);
				t[i] = solver.makeIntVar(0, 0, "t"+i);
				double cap = input.getTaxi(k-1).getCap();
				c[i] = solver.makeNumVar(cap, cap, "c"+i);
			}
			else {
				r[i] = solver.makeIntVar(1, K, "r"+i);
				t[i] = solver.makeIntVar(0, 2*N+2*M, "t"+i);
				c[i] = solver.makeNumVar(0, MPSolver.infinity(), "c"+i);
			}
		}
		
		// (16)
		for (int i=2*N+2*M+1; i<=2*N+2*M+K; i++)
			for (int j=2*N+2*M+1; j<=2*N+2*M+K; j++)
				if (i != j) {
				MPConstraint p = solver.makeConstraint(0, 0);
				p.setCoefficient(x[i][j], 1);
			}
		
		// (17)
		for (int i=1; i<=2*N+2*M; i++) {
			MPConstraint p = solver.makeConstraint(0, 0);
			p.setCoefficient(x[i][i], 1);
		}
		
		// (5) + (6)
		for (int i=1; i<=2*N+2*M+K; i++) {
			MPConstraint ij = solver.makeConstraint(1, 1);
			MPConstraint ji = solver.makeConstraint(1, 1);
			for (int j=1; j<=2*N+2*M+K; j++) {
				ij.setCoefficient(x[i][j], 1);
				ji.setCoefficient(x[j][i], 1);
			}
		}
		
		// (8)
		for (int i=1; i<=2*N+2*M+K; i++)
			for (int j=1; j<=2*N+2*M+K; j++) {
				MPConstraint pos = solver.makeConstraint(-BigM - eps, MPSolver.infinity());
				pos.setCoefficient(r[j], 1);
				pos.setCoefficient(r[i], -1);
				pos.setCoefficient(x[i][j], -BigM);
				
				MPConstraint neg = solver.makeConstraint(-MPSolver.infinity(), BigM + eps);
				neg.setCoefficient(r[j], 1);
				neg.setCoefficient(r[i], -1);
				neg.setCoefficient(x[i][j], BigM);
			}
		
		// (10)
		for (int i=1; i<=2*N+2*M+K; i++)
			for (int j=1; j<=2*N+2*M; j++) {
				MPConstraint pos = solver.makeConstraint(1 - BigM - eps, MPSolver.infinity());
				pos.setCoefficient(t[j], 1);
				pos.setCoefficient(t[i], -1);
				pos.setCoefficient(x[i][j], -BigM);
				
				MPConstraint neg = solver.makeConstraint(-MPSolver.infinity(), 1 + BigM +eps);
				neg.setCoefficient(t[j], 1);
				neg.setCoefficient(t[i], -1);
				neg.setCoefficient(x[i][j], BigM);
			}
	
		// (12)
		for (int i=1; i<=2*N+2*M+K; i++)
			for (int j=1; j<=2*N+2*M; j++) {
				MPConstraint pos = solver.makeConstraint(w[j] - BigM - eps, MPSolver.infinity());
				pos.setCoefficient(c[j], 1);
				pos.setCoefficient(c[i], -1);
				pos.setCoefficient(x[i][j], -BigM);
				
				MPConstraint neg = solver.makeConstraint(-MPSolver.infinity(), w[j] + BigM + eps);
				neg.setCoefficient(c[j], 1);
				neg.setCoefficient(c[i], -1);
				neg.setCoefficient(x[i][j], BigM);
			}
		
		// (13)
		for (int i=1; i<=N+M; i++) {
			MPConstraint p = solver.makeConstraint(0, 0);
			p.setCoefficient(r[i], 1);
			p.setCoefficient(r[i+N+M], -1);
		}
		
		// (14)
		for (int i=1; i<=N; i++) {
			MPConstraint p = solver.makeConstraint(1, 1);
			p.setCoefficient(x[i][i+N+M], 1);
		}
		
		// (15)
		for (int i=N+1; i<=N+M; i++) {
			MPConstraint p = solver.makeConstraint(-MPSolver.infinity(), -1);
			p.setCoefficient(t[i], 1);
			p.setCoefficient(t[i+N+M], -1);
		}
		
		// (18)
		MPObjective obj = solver.objective();
		for (int i=1; i<=2*N+2*M+K; i++)
			for (int j=1; j<=2*N+2*M+K; j++)
				obj.setCoefficient(x[i][j], d[i][j]);
		obj.setMinimization();
		
		// solve
		final MPSolver.ResultStatus status = solver.solve();
		
		if (status == MPSolver.ResultStatus.OPTIMAL) {
			List<List<Integer>> tours = new ArrayList<>();
			for (int k=1; k<=K; k++) {
				ArrayList<Integer> tour = new ArrayList<>();
				tour.add(0);
				int i = 2*N + 2*M + k, j;
				while (true) {
					for (j=1; j<=2*N+2*M+K; j++)
						if (solver.lookupVariableOrNull(i+"_"+j).solutionValue() == 1)
							break;
					if (j == 2*N + 2*M + k)
						break;
					tour.add(j);
					i = j;
				}
				tour.add(0);
				tours.add(tour);
			}
			MySolution solution = new MySolution();
			solution.setSolution(tours);
			solution.setScore(obj.value());
			solution.validate(input);
			System.out.println(solution.score());
			return solution;
		}
//		System.out.println(status.name());
		
		return null;
	}

	public static void main(String[] args) {
		MIP mip = new MIP(new Properties());
		mip.solve();
	}
}
