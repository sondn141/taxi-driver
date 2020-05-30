package edu.hust.soict.cbls.algorithm.ga.operators.crossover;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.ga.MyGASolution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.datastructure.Pair;
import edu.hust.soict.cbls.common.ea.ga.operator.Crossover;
import edu.hust.soict.cbls.common.utils.SolutionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OuterPassengerReorderCrossover implements Crossover<MyGASolution> {

    private Properties props;

    public OuterPassengerReorderCrossover(Properties props){
        this.props = props;
    }

    @Override
    public List<MyGASolution> execute(List<MyGASolution> parents) {
        List<List<Integer>> r1 = parents.get(0).convert();
        List<List<Integer>> r2 = parents.get(1).convert();

        Pair<Integer, Integer> homo = mostPHomologousRoute(r1, r2);

        List<Integer> reordered1 = reorderRoute(r1.get(homo.getK()), r2.get(homo.getV()));
        r2.set(homo.getV(), reordered1);
        List<Integer> reordered2 = reorderRoute(r2.get(homo.getV()), r1.get(homo.getK()));
        r1.set(homo.getK(), reordered2);

        return Arrays.asList(new MyGASolution(props, r1), new MyGASolution(props, r2));
    }

    private Pair<Integer, Integer> mostPHomologousRoute(List<List<Integer>> r1, List<List<Integer>> r2){
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        Pair<Integer, Integer> mostHomo = new Pair<>(0, 0);
        int homo = 0;

        for(int i = 0 ; i < r1.size() ; i ++){
            List<Integer> tmp = new LinkedList<>(r1.get(i));
            for(int j = 0 ; j < r2.size() ; j ++){
                tmp.retainAll(r2.get(j));
                try{
                    tmp.removeIf((k) -> {
                        int t = inp.pointType(k);
                        return t != 1 && t != 3;
                    });
                }catch (Exception e){
                    System.out.println();
                }
                if(tmp.size() > homo){
                    mostHomo.set(i, j);
                    homo = tmp.size();
                }
            }
        }

        return mostHomo;
    }

    private List<Integer> reorderRoute(List<Integer> r1, List<Integer> r2){
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        List<Integer> common = new LinkedList<>(r1);
        common.retainAll(r2);
        common.removeIf((i) -> {
            int t = inp.pointType(i);
            return t != 1 && t != 3;
        });

        List<Integer> c = new LinkedList<>(r2);
        int ptr = 0;
        for(int i = 0 ; i < c.size() ; i ++){
            if(common.contains(c.get(i))){
                c.set(i, common.get(ptr++));
            }
        }
        return c;
    }
}
