package edu.hust.soict.cbls.worker;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;
import edu.hust.soict.cbls.common.utils.StringUtils;
import edu.hust.soict.cbls.data.Writer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecursiveWorker {

    private Properties props;
    private File output;
    private File input;

    private List<Solver> solvers;

    private static final Logger logger = LoggerFactory.getLogger(RecursiveWorker.class);

    public RecursiveWorker(Properties props) throws IOException {
        this.props = props;
        this.input = new File(props.getProperty(Const.INPUT));
        this.output = new File(props.getProperty(Const.OUTPUT));

        if(!input.exists())
            throw new RuntimeException("Input does not exist");
        if(input.isDirectory()^output.isDirectory())
            throw new RuntimeException("Input and Output should be the same type, single files or directories");
        if(StringUtils.validPath(output.getAbsolutePath()))
            throw new RuntimeException("Invalid output path");

        if(input.isDirectory())
            FileUtils.copyDirectory(input, output, (File f) -> false);
    }

    private void recursiveWork(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files == null) return;
            for(File f : files)
                recursiveWork(f);
        } else{
            String inpFile = file.getAbsolutePath();
            String outFile = inpFile.replace(input.getAbsolutePath(), output.getAbsolutePath());

            List<String> solverClazz = props.getCollection(Const.SOLVER_CLASS);
            props.setProperty(Const.INPUT, inpFile);
            this.solvers = new LinkedList<>();
            for(String clazz : solverClazz){
                solvers.add(Reflects.newInstance(clazz, new Class[]{Properties.class}, props));
            }

            ExecutorService executor = Executors.newFixedThreadPool(props.getIntProperty(Const.WORKER_THREAD_POOL_SIZE, 5));
            for(Solver solver : solvers){
                solver.setInput(inpFile);
                executor.submit(() -> {
                    try{
                        long s = System.currentTimeMillis();
                        Solution solution = solver.solve();
                        long runtime = System.currentTimeMillis() - s;
                        Writer.write(solution, outFile, solver.getClass(), runtime, true);
                    } catch (Exception e){
                        logger.error("Error while solving problem", e);
                    }
                });
            }
            executor.shutdown();
        }
    }

    public void work(){
        recursiveWork(input);
    }


    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        RecursiveWorker worker = new RecursiveWorker(props);

        worker.work();
    }
}
