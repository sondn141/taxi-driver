package edu.hust.soict.cbls.visualization;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.data.Reader;
import edu.hust.soict.cbls.entity.Point;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InputDrawer implements Visualizer {

    private Properties props;
    private Input input;

    private static final Logger logger = LoggerFactory.getLogger(InputDrawer.class);

    public InputDrawer(Properties props){
        this.props = props;
        this.input = Reader.read(props.getProperty(Const.VISUALIZATION_INPUT_FILE));
    }

    @Override
    public void drawAndSave() {
        JFreeChart bubbleChart = ChartFactory.createBubbleChart(
                "AGE vs WEIGHT vs WORK",
                "Weight",
                "AGE",
                createDataset(),
                PlotOrientation.HORIZONTAL,
                true, true, false);

        XYPlot xyplot = ( XYPlot )bubbleChart.getPlot( );
        xyplot.setForegroundAlpha( 0.65F );
        XYItemRenderer xyitemrenderer = xyplot.getRenderer( );
        xyitemrenderer.setSeriesPaint( 0 , Color.BLUE );
        NumberAxis numberaxis = ( NumberAxis )xyplot.getDomainAxis( );
        numberaxis.setLowerMargin( 0.2 );
        numberaxis.setUpperMargin( 0.5 );
        NumberAxis numberaxis1 = ( NumberAxis )xyplot.getRangeAxis( );
        numberaxis1.setLowerMargin( 0.8 );
        numberaxis1.setUpperMargin( 0.9 );

        try {
            ChartUtilities.saveChartAsPNG(
                    new File(props.getProperty(Const.VISUALIZATION_IMAGE_FOLDER) +
                                    props.getProperty(Const.VISUALIZATION_IMAGE_FILE_NAME)),
                    bubbleChart,
                    props.getIntProperty(Const.VISUALIZATION_FIGURE_WIDTH, 900),
                    props.getIntProperty(Const.VISUALIZATION_FIGURE_HEIGHT, 900));
        } catch (IOException e) {
            logger.error("Error while creating objective visualization chart", e);
        }
    }

    private XYZDataset createDataset(){
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
        int size = input.size();
        double[][] coors = new double[3][size];

        for(int i = 0 ; i < size ; i ++){
            Point p = input.point(i);
            coors[0][i] = p.getX();
            coors[1][i] = p.getY();
            coors[2][i] = 10;
        }

        defaultxyzdataset.addSeries("Input", coors);
        return defaultxyzdataset;
    }
}
