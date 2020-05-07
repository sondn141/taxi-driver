package edu.hust.soict.cbls.algorithm.ga;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.algorithm.ga.components.Population;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GASolver<I extends Solution> extends Solver {
    private Class<I> solClazz;
    private Population<I> pop;
    private Properties props;

    private static final Logger logger = LoggerFactory.getLogger(GASolver.class);

    public GASolver(Properties props, Class<I> solClazz) {
        super(props);
        this.solClazz = solClazz;
        this.props = props;
    }

    @Override
    protected Solution solve() {
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
