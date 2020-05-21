package edu.hust.soict.cbls.visualization;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.datastructure.Pair;
import edu.hust.soict.cbls.data.Reader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiInputMultiSolverViz implements Visualizer {

    private Properties props;
    private List<Pair<String, String>> inpSolPairs;

    private static final Logger logger = LoggerFactory.getLogger(MultiInputMultiSolverViz.class);

    public MultiInputMultiSolverViz(Properties props){
        this.props = props;
        this.inpSolPairs = new LinkedList<>();
        List<String> inps = props.getCollection(Const.VISUALIZATION_INPUT_FILE);
        List<String> outs = props.getCollection(Const.VISUALIZATION_RESULT_FILE);

        if(inps.size() != outs.size())
            throw new RuntimeException("Number of input files does not match number of result files");

        int size = inps.size();
        for(int i = 0 ; i < size ; i ++){
            inpSolPairs.add(new Pair<>(inps.get(i), outs.get(i)));
        }
    }

    @Override
    public void drawAndSave() {
        JFreeChart lineChart = ChartFactory.createLineChart(
                props.getProperty(Const.VISUALIZATION_CHART_TITLE),
                "Years","Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true,true,false);

        try {
            ChartUtilities.saveChartAsPNG(new File(
                            props.getProperty(Const.VISUALIZATION_IMAGE_FOLDER) +
                                    props.getProperty(Const.VISUALIZATION_IMAGE_FILE_NAME)),
                    lineChart,
                    props.getIntProperty(Const.VISUALIZATION_FIGURE_WIDTH, 900),
                    props.getIntProperty(Const.VISUALIZATION_FIGURE_HEIGHT, 900));
        } catch (IOException e) {
            logger.error("Error while creating objective visualization chart", e);
        }
    }

    private CategoryDataset createDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Map<String, Solution>> inpSolMap = Reader.readMultiInputsAndSolutions(inpSolPairs);
        for(Map.Entry<String, Map<String, Solution>> kv : inpSolMap.entrySet()){
            String input = kv.getKey();
            Map<String, Solution> mapSol = kv.getValue();
            for(Map.Entry<String, Solution> sols : mapSol.entrySet()){
                String solver = sols.getKey();
                double score = sols.getValue().score();
                dataset.addValue(score, solver, input);
            }
        }

        return dataset;
    }
}
