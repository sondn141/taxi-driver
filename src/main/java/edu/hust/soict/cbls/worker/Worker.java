package edu.hust.soict.cbls.worker;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.datastructure.Point;
import edu.hust.soict.cbls.common.utils.Reflects;
import edu.hust.soict.cbls.data.Reader;
import edu.hust.soict.cbls.data.Writer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Worker implements Runnable {

    private List<Solver> solvers;
    private String output;

    public Worker(Properties props){
        List<String> solverClazz = props.getCollection(Const.SOLVER_CLASS);
        this.output = props.getProperty(Const.OUTPUT_FILE_PATH);
        this.solvers = new LinkedList<>();
        for(String clazz : solverClazz){
            solvers.add(Reflects.newInstance(clazz, new Class[]{Properties.class}, props));
        }
    }

    @Override
    public void run(){
        for(Solver solver : solvers){
            Solution solution = solver.solve();
            Writer.write(solution, output, solver.getClass(), true);
        }
    }


    public static void main(String[] args) {
        Properties props = new Properties();
        Worker worker = new Worker(props);

        worker.run();
    }
}
