package edu.hust.soict.cbls.algorithm.ga.operator;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;

import java.util.List;

public interface NaturalSelection<I extends Solution> {

    List<I> execute(List<I> population);

    static <T extends NaturalSelection<?>> T getNaturalSelection(Properties props){
        String selectParentClazz = props.getProperty(Const.GA_NATURAL_SELECTION_OPERATOR_CLASS);
        return Reflects.newInstance(selectParentClazz, new Class<?>[]{Properties.class}, props);
    }

}
