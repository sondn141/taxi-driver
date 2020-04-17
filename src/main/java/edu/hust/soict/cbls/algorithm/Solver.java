package edu.hust.soict.cbls.algorithm;

import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.exception.CommonException;
import edu.hust.soict.cbls.common.io.Reader;
import edu.hust.soict.cbls.common.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class Solver implements Runnable{

    protected Input input;
    protected Properties props;

    private static final Logger logger = LoggerFactory.getLogger(Solver.class);

    public Solver(Properties props){
        this.props = props;
        try {
            this.input = Reader.read(Const.INPUT_FILE_PATH);
        } catch (IOException e) {
            logger.error(CommonException.getMessage(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        Output output = solve(this.input);
        Writer.write(output, props.getProperty(Const.OUTPUT_FILE_PATH));
    }

    public abstract Output solve(Input data);

}
