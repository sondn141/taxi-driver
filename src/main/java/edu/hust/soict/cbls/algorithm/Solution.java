package edu.hust.soict.cbls.algorithm;

import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;

import java.util.List;

public interface Solution extends Comparable<Solution>{

    static <T extends Solution> T getIndividual(Properties props, Class<T> clazz){
        return Reflects.newInstance(clazz, new Class[]{Properties.class}, props);
    }

    @Override
    default int compareTo(Solution other) {
        return Double.compare(this.score(), other.score());
    }

    List<List<Integer>> convert();

    double score();

}
