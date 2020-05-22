package edu.hust.soict.cbls.algorithm;

import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.data.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class Solver {

    protected Input input;
    protected Properties props;

    private static final Logger logger = LoggerFactory.getLogger(Solver.class);

    public Solver(Properties props){
        this.props = props;
        try {
            this.input = Reader.read(props.getProperty(Const.INPUT));
        } catch (IOException e) {
            logger.warn("Can not read data from input file. May be using recursive worker");
        }
    }

    public abstract Solution solve();

    public void setInput(String inp){
        try {
            this.input = Reader.read(inp);
        } catch (IOException e) {
            logger.error("Can not read input from " + inp);
        }
    }

}
