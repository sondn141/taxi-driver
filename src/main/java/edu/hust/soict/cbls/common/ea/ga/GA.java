package edu.hust.soict.cbls.common.ea.ga;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.common.ea.ga.components.Population;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GA<I extends Solution> extends Solver {
    private Class<I> solClazz;
    private Population<I> pop;
    private Properties props;

    private static final Logger logger = LoggerFactory.getLogger(GA.class);

    public GA(Properties props, Class<I> solClazz) {
        super(props);
        this.solClazz = solClazz;
        this.props = props;
    }

    @Override
    public Solution solve() {
        logger.info("Start genetic algorithm...");
        this.pop = new Population<>(props, solClazz);
        logger.info("Initialized population. Tending to evolute the population.");
        for(int i = 1 ; i <= props.getIntProperty(Const.GA_GENERATION, 100); i ++){
            logger.info("=========================== Gen " + i + " ===========================");
            List<I> childs = pop.crossover();
            pop.mutation(childs);
            pop.addAll(childs);
            pop.sort();
            pop.naturalSelection();
            logger.info("==============================================================");
        }

        return pop.get(0);
    }
}
