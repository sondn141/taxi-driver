package edu.hust.soict.cbls.algorithm.ga;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.CollectionUtils;
import edu.hust.soict.cbls.common.utils.RandomUtils;
import edu.hust.soict.cbls.common.utils.SolutionUtils;
import edu.hust.soict.cbls.entity.Commodity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyGASolution implements Solution {

    private List<List<Integer>> gene;
    private double score;

    public MyGASolution(Properties props){
        // TODO: Initialize solution here
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        this.gene = new LinkedList<>();
        int k = inp.getTaxis().size();
        for(int i = 0 ; i < k ; i ++){
            gene.add(new LinkedList<>());
        }

        List<Commodity> commodities = inp.getCommodities();
        for(int i = 0 ; i < commodities.size() ; i ++){
            int route = RandomUtils.randInt(0, k);
            int pickup = SolutionUtils.firstPossiblePickupIndex(inp, commodities.get(i), gene.get(route), inp.getTaxi(route).getCap());
            gene.get(route).add(pickup, inp.commodityPointIdx(i, 2));
            int deliver = RandomUtils.randInt(pickup + 1, gene.get(route).size() + 1);
            gene.get(route).add(deliver, inp.commodityPointIdx(i, 4));
        }

        for(int i = 1 ; i <= inp.getPassengers().size() ; i ++){
            int route = RandomUtils.randInt(0, k);
            int slot = RandomUtils.randInt(0, gene.get(route).size() + 1);
            gene.get(route).add(slot, i);
            gene.get(route).add(slot + 1, i + inp.getPassengers().size() + commodities.size());
        }

        this.score = SolutionUtils.evaluate(inp, this.gene);
    }

    public MyGASolution(Properties props, List<List<Integer>> gene){
        this.gene = gene;
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        this.score = SolutionUtils.evaluate(inp, this.gene);
    }

    @Override
    public List<List<Integer>> convert() {
        return gene;
    }

    @Override
    public double score() {
        return score;
    }
}
