package edu.hust.soict.cbls.common.ea.ga.operator;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;

import java.util.List;

public interface SelectParents<I extends Solution> {

    List<List<I>> execute(List<I> population);

    static <T extends SelectParents<?>> T getSelectParent(Properties props){
        String selectParentClazz = props.getProperty(Const.GA_SELECT_PARENT_OPERATOR_CLASS);
        return Reflects.newInstance(selectParentClazz, new Class<?>[]{Properties.class}, props);
    }

}
