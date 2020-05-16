package edu.hust.soict.cbls.visualization;

import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.data.Reader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SingleInputMultiSolversViz implements Visualizer{

    private Properties props;
    private Map<String, Solution> mapSolution;

    private static final Logger logger = LoggerFactory.getLogger(SingleInputMultiSolversViz.class);

    public SingleInputMultiSolversViz(Properties props){
        this.props = props;
        this.mapSolution = Reader.readSolution(props.getProperty(Const.VISUALIZATION_RESULT_FILE));
    }

    @Override
    public void drawAndSave(){
        JFreeChart barChart = ChartFactory.createBarChart(
                props.getProperty(Const.SINGLE_INPUT_NULTI_SOLVERS_CHART_TITLE),
                "Solvers",
                "Objective",
                createObjectiveDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        BarRenderer barR = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        barR.setMaximumBarWidth(0.2);

        try {
            ChartUtilities.saveChartAsPNG(new File(
                    props.getProperty(Const.VISUALIZATION_IMAGE_FOLDER) +
                    props.getProperty(Const.VISUALIZATION_IMAGE_FILE_NAME)),
                    barChart,
                    props.getIntProperty(Const.VISUALIZATION_FIGURE_WIDTH, 900),
                    props.getIntProperty(Const.VISUALIZATION_FIGURE_HEIGHT, 900));
        } catch (IOException e) {
            logger.error("Error while creating objective visualization chart", e);
        }
    }

    private CategoryDataset createObjectiveDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(Map.Entry<String, Solution> kv : mapSolution.entrySet()){
            String solver = kv.getKey();
            String groupId = solver.substring(solver.lastIndexOf(".") + 1);
            double score = kv.getValue().score();

            dataset.addValue(score, groupId, "");
        }

        return dataset;
    }
}
