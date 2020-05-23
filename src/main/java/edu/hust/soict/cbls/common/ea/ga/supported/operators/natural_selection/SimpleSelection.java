package edu.hust.soict.cbls.common.ea.ga.supported.operators.natural_selection;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.ea.ga.operator.NaturalSelection;

import java.util.List;

public class SimpleSelection implements NaturalSelection<Solution> {

    private Properties props;

    public SimpleSelection(Properties props){
        this.props = props;
    }

    @Override
    public List<Solution> execute(List<Solution> population) {
        return population.subList(0, this.props.getIntProperty(Const.GA_POPULATION_SIZE, 100));
    }
}
