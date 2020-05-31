package edu.hust.soict.cbls.algorithm.ga.operators.mutation;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.ga.MyGASolution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.datastructure.Triplet;
import edu.hust.soict.cbls.common.ea.ga.operator.Mutation;
import edu.hust.soict.cbls.common.utils.CollectionUtils;
import edu.hust.soict.cbls.common.utils.SolutionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PassengerSwapMutation implements Mutation<MyGASolution> {

    private Properties props;

    private static final Logger logger = LoggerFactory.getLogger(PassengerSwapMutation.class);

    public PassengerSwapMutation(Properties props){
        this.props = props;
    }

    @Override
    public MyGASolution execute(MyGASolution ind) {
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        List<List<Integer>> routes = ind.convert();

        List<Triplet<Integer, Integer, Integer>> pIdxes = pIndexes(routes);
        Collections.shuffle(pIdxes);

        Triplet<Integer, Integer, Integer> p1 = pIdxes.get(0);
        Triplet<Integer, Integer, Integer> p2 = pIdxes.get(1);

        routes.get(p1.get2()).set(p1.get3(), p2.get1());
        routes.get(p1.get2()).set(p1.get3() + 1, inp.getPassengerGetOff(p2.get1()));
        routes.get(p2.get2()).set(p2.get3(), p1.get1());
        routes.get(p2.get2()).set(p2.get3() + 1, inp.getPassengerGetOff(p1.get1()));

        if(!SolutionUtils.validateSolution(routes, inp))
            logger.warn("Invalid solution produced");

        return new MyGASolution(props, routes);
    }

    public List<Triplet<Integer, Integer, Integer>> pIndexes(List<List<Integer>> routes){
        List<Triplet<Integer, Integer, Integer>> idxes = new LinkedList<>();
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        for(int i = 0 ; i < routes.size() ; i ++){
            for(int j = 0 ; j < routes.get(i).size() ; j ++){
                int p = routes.get(i).get(j);
                int type = inp.pointType(p);
                if(type == 1)
                    idxes.add(new Triplet<>(p, i, j));
            }
        }

        return idxes;
    }
}
