package edu.hust.soict.cbls.algorithm.ga.operators.crossover;

import edu.hust.soict.cbls.algorithm.ga.MyGASolution;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.ea.ga.operator.Crossover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SwapCommodityCrossover implements Crossover<MyGASolution> {

    private static final Logger logger = LoggerFactory.getLogger(SwapCommodityCrossover.class);

    private Properties props;

    public SwapCommodityCrossover(Properties props){
        this.props = props;
    }

    @Override
    public List<MyGASolution> execute(List<MyGASolution> parents) {
        List<List<Integer>> dad = parents.get(0).convert();
        List<List<Integer>> mom = parents.get(1).convert();



        return null;
    }
}
