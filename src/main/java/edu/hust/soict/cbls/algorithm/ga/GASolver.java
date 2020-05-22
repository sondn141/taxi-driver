package edu.hust.soict.cbls.algorithm.ga;

import edu.hust.soict.cbls.algorithm.ga.components.MyGASolution;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.ea.ga.GA;

public class GASolver extends GA<MyGASolution> {

    public GASolver(Properties props, Class<MyGASolution> solClazz) {
        super(props, solClazz);
    }
}
