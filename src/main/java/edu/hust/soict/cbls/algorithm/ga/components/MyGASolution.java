package edu.hust.soict.cbls.algorithm.ga.components;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Properties;

import java.util.List;

public class MyGASolution implements Solution {

    private Properties props;
    private int[] gene;

    public MyGASolution(Properties props, int n){
        this.props = props;
    }

    public MyGASolution(Properties props, int[] gene){
        this.props = props;
        this.gene = gene;
    }

    @Override
    public List<List<Integer>> convert() {
        return null;
    }

    @Override
    public double score() {
        return 0;
    }
}
