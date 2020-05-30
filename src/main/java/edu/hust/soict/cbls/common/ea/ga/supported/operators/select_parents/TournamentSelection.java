package edu.hust.soict.cbls.common.ea.ga.supported.operators.select_parents;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.ea.ga.operator.SelectParents;
import edu.hust.soict.cbls.common.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

        List<List<Solution>> listOfParents = new ArrayList<>();
        for(int i = 0 ; i < numPairs ; i ++){
            int[] indexOfSelectedIndividual = RandomUtils.randInts(0,
                    props.getIntProperty(Const.GA_POPULATION_SIZE, 200), tournamentSize);
            List<Solution> tounamentList = new ArrayList<>();
            for(Integer index : indexOfSelectedIndividual){
                tounamentList.add(population.get(index));
            }
            Collections.sort(tounamentList);
            listOfParents.add(Arrays.asList(tounamentList.get(0), tounamentList.get(1)));
        }

        return listOfParents;
    }
}
