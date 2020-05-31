package edu.hust.soict.cbls.algorithm.ga.operators.mutation;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.ga.MyGASolution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.ea.ga.operator.Mutation;
import edu.hust.soict.cbls.common.utils.RandomUtils;
import edu.hust.soict.cbls.common.utils.SolutionUtils;
import edu.hust.soict.cbls.entity.Commodity;
import edu.hust.soict.cbls.entity.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommodityMoveMutation implements Mutation<MyGASolution> {

    private Properties props;

    private static final Logger logger = LoggerFactory.getLogger(CommodityMoveMutation.class);

    public CommodityMoveMutation(Properties props){
        this.props = props;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public MyGASolution execute(MyGASolution ind) {
        List<List<Integer>> routes = ind.convert();

        Supplier<IntStream> sizeStr = () -> routes.stream().mapToInt(List::size);
        List<Integer> sizeList = sizeStr.get().boxed().collect(Collectors.toList());
        int longest = sizeList.indexOf(sizeStr.get().max().getAsInt());
        int shortest = sizeList.indexOf(sizeStr.get().min().getAsInt());

        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        Point meanS = SolutionUtils.meanOfRoute(routes.get(shortest), inp);
        int chosenC = -1;
        double nearest = Double.MAX_VALUE;
        for(Integer i : routes.get(longest)){
            int pointType = inp.pointType(i);
            if(pointType == 2){
                double dis = meanS.distance(inp.point(i));
                if(dis < nearest){
                    chosenC = i;
                    nearest = dis;
                }
            }
        }

        Commodity c = inp.getCommodity(chosenC);
        int deliver = c.getDeliver().getIdx();
        int slot = SolutionUtils.possiblePickupIndex(inp, c, routes.get(shortest), inp.getCap(shortest));
        if(slot >= 0){
            routes.get(longest).remove(new Integer(chosenC));
            routes.get(longest).remove(new Integer(deliver));
            routes.get(shortest).add(slot, chosenC);
            int deliNewSlot = -1;
            if(routes.get(shortest).size() == 3)
                deliNewSlot = slot + 1;
            else
                while(deliNewSlot < 0){
                    deliNewSlot = RandomUtils.randInt(slot + 1, routes.get(shortest).size());
                    int t = inp.pointType(routes.get(shortest).get(deliNewSlot));
                    if(t != 1 && t != 3){
                        break;
                    }
                    else deliNewSlot = -1;
                }
            routes.get(shortest).add(deliNewSlot, deliver);
        }


        if(!SolutionUtils.validateSolution(routes, inp))
            logger.warn("Invalid solution produced");

        return new MyGASolution(props, routes);
    }
}
