package edu.hust.soict.cbls.algorithm.ga;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.CollectionUtils;
import edu.hust.soict.cbls.common.utils.RandomUtils;
import edu.hust.soict.cbls.common.utils.SolutionUtils;
import edu.hust.soict.cbls.entity.Commodity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyGASolution implements Solution {

    private List<List<Integer>> gene;
    private double score;

    private static final Logger logger = LoggerFactory.getLogger(MyGASolution.class);

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
            int pickup = -1;
            int route = -1;
            while(pickup < 0){
                route = RandomUtils.randInt(0, k);
                pickup = SolutionUtils.possiblePickupIndex(inp, commodities.get(i), gene.get(route), inp.getTaxi(route).getCap());
            }
            if(gene.get(route).isEmpty())
                gene.get(route).add(inp.commodityPointIdx(i, 2));
            else
                gene.get(route).add(pickup, inp.commodityPointIdx(i, 2));
            int deliver = RandomUtils.randInt(pickup + 1, gene.get(route).size() + 1);
            gene.get(route).add(deliver, inp.commodityPointIdx(i, 4));
        }

        for(int i = 1 ; i <= inp.getPassengers().size() ; i ++){
            int route = RandomUtils.randInt(0, k);
            if(gene.get(route).isEmpty()){
                gene.get(route).add(i);
                gene.get(route).add(i + inp.getPassengers().size() + commodities.size());
                continue;
            }
            int slot = RandomUtils.randInt(0, gene.get(route).size() + 1);
            if(CollectionUtils.inRangeInclusive(0, gene.get(route).size() - 1, slot)){
                int type = inp.pointType(gene.get(route).get(slot));
                slot += type == 1 ? 2 : (type == 3 ? 1 : 0);
            }
            gene.get(route).add(slot, i);
            gene.get(route).add(slot + 1, i + inp.getPassengers().size() + commodities.size());
        }

        for(List<Integer> r : gene){
            r.add(0);
            r.add(0, 0);
        }

        if(!SolutionUtils.validateSolution(gene, inp))
            logger.warn("Invalid solution generated");

        this.score = SolutionUtils.evaluate(inp, this.gene);
    }

    public MyGASolution(Properties props, List<List<Integer>> gene){
        this.gene = gene;
        Input inp = props.getRuntimeObject(Const.INPUT_OBJECT, Input.class);
        this.score = SolutionUtils.evaluate(inp, this.gene);
    }

    @Override
    public List<List<Integer>> convert() {
        List<List<Integer>> n = new LinkedList<>();
        for(List<Integer> l : gene){
            List<Integer> k = new LinkedList<>(l);
            n.add(k);
        }

        return n;
    }

    @Override
    public double score() {
        return score;
    }
}
