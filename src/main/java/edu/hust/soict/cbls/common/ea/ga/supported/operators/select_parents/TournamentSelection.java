package edu.hust.soict.cbls.common.ea.ga.supported.operators.select_parents;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.ea.ga.operator.SelectParents;

import java.util.List;

public class TournamentSelection implements SelectParents<Solution> {

    private Properties props;

    public TournamentSelection(Properties props){
        this.props = props;
    }

    @Override
    public List<List<Solution>> execute(List<Solution> population) {
        int numPairs = props.getIntProperty(Const.GA_SELECT_PARENT_NUM_OF_PAIRS, 50);
        int tournamentSize = props.getIntProperty(Const.GA_SELECT_PARENT_TOURNAMENT_SIZE, 5);
        return null;
    }
}
