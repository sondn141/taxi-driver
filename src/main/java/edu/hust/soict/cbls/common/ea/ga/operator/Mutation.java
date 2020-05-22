package edu.hust.soict.cbls.common.ea.ga.operator;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;

import java.util.LinkedList;
import java.util.List;

public interface Mutation<I extends Solution> {

    I execute(I ind);

    static <T extends Mutation> List<T> getMutation(Properties props){
        List<String> mutationClazz = props.getCollection(Const.GA_MUTATION_OPERATOR_CLASS);
        List<T> oprs = new LinkedList<>();
        for(String className : mutationClazz){
            oprs.add(Reflects.newInstance(className, new Class<?>[]{Properties.class}, props));
        }

        return oprs;
    }

}
