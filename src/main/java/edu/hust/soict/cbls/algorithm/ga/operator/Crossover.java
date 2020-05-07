package edu.hust.soict.cbls.algorithm.ga.operator;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;

import java.util.LinkedList;
import java.util.List;

public interface Crossover<I extends Solution> {

    List<I> execute(List<I> parents);

    static <T extends Crossover<?>> List<T> getCrossover(Properties props){
        List<String> crossoverClazz = props.getCollection(Const.GA_CROSSOVER_OPERATOR_CLASS);
        List<T> oprs = new LinkedList<>();
        for(String className : crossoverClazz){
            oprs.add(Reflects.newInstance(className, new Class<?>[]{Properties.class}, props));
        }

        return oprs;
    }

}
